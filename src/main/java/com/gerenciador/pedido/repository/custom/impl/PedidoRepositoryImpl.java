package com.gerenciador.pedido.repository.custom.impl;

import com.gerenciador.dashboard.record.FaturamentoMensalRecord;
import com.gerenciador.dashboard.record.TicketMedioRecord;
import com.gerenciador.dashboard.record.TopCincoRecord;
import com.gerenciador.pedido.entity.Pedido;
import com.gerenciador.pedido.enums.StatusPedido;
import com.gerenciador.pedido.record.PedidoResumoRecord;
import com.gerenciador.pedido.repository.custom.PedidoRepositoryCustom;
import com.gerenciador.produto.record.ProdutoRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Repository
public class PedidoRepositoryImpl implements PedidoRepositoryCustom {


    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<PedidoResumoRecord> buscarPedidosComProdutos(
            String usuario,
            String status,
            Pageable pageable
    ) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
        SELECT 
            p.valor_total AS valor_total,
            p.id AS pedido_id,
            p.usuario AS usuario,
            p.status AS status,
            pr.id AS produto_id,
            pr.nome AS produto_nome,
            pr.descricao AS produto_descricao,
            pr.preco AS produto_preco,
            pr.id_categoria AS id_categoria,
            pr.quantidade_estoque AS quantidade_estoque,
            pi.quantidade AS quantidade_produto
        FROM pedido p
        JOIN pedido_itens pi ON p.id = pi.id_pedido
        JOIN produto pr ON pi.id_produto = pr.id
        WHERE 1=1
    """);

        if (usuario != null && !usuario.isEmpty()) {
            sql.append(" AND p.usuario = :usuario");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND p.status = :status");
        }

        sql.append(" ORDER BY p.data_pedido DESC");

        StringBuilder countSql = new StringBuilder();
        countSql.append("SELECT COUNT(DISTINCT p.id) FROM pedido p WHERE 1=1");

        if (usuario != null && !usuario.isEmpty()) {
            countSql.append(" AND p.usuario = :usuario");
        }
        if (status != null && !status.isEmpty()) {
            countSql.append(" AND p.status = :status");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        if (usuario != null && !usuario.isEmpty()) {
            query.setParameter("usuario", usuario);
            countQuery.setParameter("usuario", usuario);
        }
        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
            countQuery.setParameter("status", status);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        // ✅ Montagem do resultado agrupado por pedido
        Map<Long, PedidoResumoRecord> pedidosMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            BigDecimal valorTotal = (BigDecimal) row[0];
            Long pedidoId = ((Number) row[1]).longValue();
            String user = (String) row[2];
            StatusPedido statusPedido = StatusPedido.valueOf((String) row[3]);

            ProdutoRecord produto = new ProdutoRecord(
                    ((Number) row[4]).longValue(),
                    (String) row[5],
                    (String) row[6],
                    (BigDecimal) row[7],
                    ((Number) row[8]).longValue(),
                    ((Number) row[9]).intValue()
            );

            Integer quantidadeProduto = ((Number) row[10]).intValue();

            pedidosMap
                    .computeIfAbsent(pedidoId, id -> new PedidoResumoRecord(
                            valorTotal,
                            pedidoId,
                            user,
                            statusPedido,
                            new ArrayList<>(),
                            quantidadeProduto
                    ))
                    .produtos()
                    .add(produto);
        }

        return new PageImpl<>(new ArrayList<>(pedidosMap.values()), pageable, total);
    }

    @Override
    public List<TopCincoRecord> buscarTopCincoUsuarios() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Pedido> root = cq.from(Pedido.class);

        Expression<String> usuario = root.get("usuario");
        Expression<Long> totalPedidos = cb.count(root.get("id"));
        Expression<BigDecimal> totalGasto = cb.sum(root.get("valorTotal"));

        cq.multiselect(usuario, totalPedidos, totalGasto);
        cq.where(cb.equal(root.get("status"), StatusPedido.CONCLUIDO));
        cq.groupBy(usuario);
        cq.orderBy(cb.desc(totalGasto));

        TypedQuery<Object[]> query = entityManager.createQuery(cq);
        query.setMaxResults(5);

        List<Object[]> resultList = query.getResultList();

        return resultList.stream()
                .map(row -> new TopCincoRecord(
                        (String) row[0],
                        (Long) row[1],
                        (BigDecimal) row[2]
                ))
                .toList();
    }

    @Override
    public List<TicketMedioRecord> buscarTicketMedioPorUsuario() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Pedido> root = cq.from(Pedido.class);

        Expression<String> usuario = root.get("usuario");
        Expression<BigDecimal> soma = cb.sum(root.get("valorTotal"));
        Expression<Long> count = cb.count(root.get("id"));

        // Calcula o ticket médio (SUM / COUNT)
        Expression<Number> ticketMedio = cb.quot(soma, cb.toBigDecimal(count));

        cq.multiselect(usuario, ticketMedio);
        cq.where(cb.equal(root.get("status"), StatusPedido.CONCLUIDO));
        cq.groupBy(usuario);
        cq.orderBy(cb.desc(ticketMedio));

        TypedQuery<Object[]> query = entityManager.createQuery(cq);
        List<Object[]> resultList = query.getResultList();

        return resultList.stream()
                .map(row -> new TicketMedioRecord(
                        (String) row[0],
                        // arredonda manualmente com scale 2 (ROUND)
                        ((BigDecimal) row[1]).setScale(2, BigDecimal.ROUND_HALF_UP)
                ))
                .toList();
    }

    @Override
    public FaturamentoMensalRecord buscarFaturamentoMensal(LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Pedido> root = cq.from(Pedido.class);

        Expression<Integer> mesExpr = cb.function("MONTH", Integer.class, root.get("dataPagamento"));
        Expression<Integer> anoExpr = cb.function("YEAR", Integer.class, root.get("dataPagamento"));
        Expression<BigDecimal> somaExpr = cb.sum(root.get("valorTotal"));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("status"), StatusPedido.CONCLUIDO));

        if (startDate != null && endDate != null) {
            Date inicio = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date fim = Date.from(endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            predicates.add(cb.between(root.get("dataPagamento"), inicio, fim));
        } else {

            predicates.add(
                    cb.equal(cb.function("MONTH", Integer.class, root.get("dataPagamento")),
                            cb.function("MONTH", Integer.class, cb.currentDate()))
            );
            predicates.add(
                    cb.equal(cb.function("YEAR", Integer.class, root.get("dataPagamento")),
                            cb.function("YEAR", Integer.class, cb.currentDate()))
            );
        }

        cq.multiselect(mesExpr, anoExpr, somaExpr);
        cq.where(predicates.toArray(new Predicate[0]));
        cq.groupBy(anoExpr, mesExpr);

        TypedQuery<Object[]> query = entityManager.createQuery(cq);
        List<Object[]> resultList = query.getResultList();

        if (resultList.isEmpty()) {
            return null;
        }

        Object[] row = resultList.get(0);
        return new FaturamentoMensalRecord(
                (Integer) row[0],
                (Integer) row[1],
                (BigDecimal) row[2]
        );
    }



}