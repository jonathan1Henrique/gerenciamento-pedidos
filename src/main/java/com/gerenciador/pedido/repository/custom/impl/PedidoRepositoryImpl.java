package com.gerenciador.pedido.repository.custom.impl;

import com.gerenciador.pedido.enums.StatusPedido;
import com.gerenciador.pedido.record.PedidoResumoRecord;
import com.gerenciador.pedido.repository.custom.PedidoRepositoryCustom;
import com.gerenciador.produto.record.ProdutoRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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



}