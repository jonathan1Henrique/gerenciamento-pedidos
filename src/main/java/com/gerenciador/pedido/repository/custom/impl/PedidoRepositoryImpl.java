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
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class PedidoRepositoryImpl implements PedidoRepositoryCustom {


    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<PedidoResumoRecord> buscarPedidosComProdutos(
            String usuarioId,
            String status,
            LocalDateTime dataInicioPedido,
            LocalDateTime dataFimPedido,
            LocalDateTime dataInicioPagamento,
            LocalDateTime dataFimPagamento,
            Pageable pageable
    ) {
        String baseQuery = """
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
        WHERE (:usuarioId IS NULL OR p.usuario = :usuarioId)
          AND (:status IS NULL OR p.status = :status)
          AND (:dataInicioPedido IS NULL OR p.data_pedido >= :dataInicioPedido)
          AND (:dataFimPedido IS NULL OR p.data_pedido <= :dataFimPedido)
          AND (:dataInicioPagamento IS NULL OR p.data_pagamento >= :dataInicioPagamento)
          AND (:dataFimPagamento IS NULL OR p.data_pagamento <= :dataFimPagamento)
        ORDER BY p.data_pedido DESC
    """;

        String countQueryStr = """
        SELECT COUNT(DISTINCT p.id)
        FROM pedido p
        WHERE (:usuarioId IS NULL OR p.usuario = :usuarioId)
          AND (:status IS NULL OR p.status = :status)
          AND (:dataInicioPedido IS NULL OR p.data_pedido >= :dataInicioPedido)
          AND (:dataFimPedido IS NULL OR p.data_pedido <= :dataFimPedido)
          AND (:dataInicioPagamento IS NULL OR p.data_pagamento >= :dataInicioPagamento)
          AND (:dataFimPagamento IS NULL OR p.data_pagamento <= :dataFimPagamento)
    """;

        Query query = entityManager.createNativeQuery(baseQuery);
        Query countQuery = entityManager.createNativeQuery(countQueryStr);

        // ✅ Use HashMap — permite valores nulos e é seguro
        Map<String, Object> params = new HashMap<>();
        params.put("usuarioId", usuarioId);
        params.put("status", status);
        params.put("dataInicioPedido", dataInicioPedido);
        params.put("dataFimPedido", dataFimPedido);
        params.put("dataInicioPagamento", dataInicioPagamento);
        params.put("dataFimPagamento", dataFimPagamento);

        // ✅ Sempre define todos os parâmetros (mesmo que nulos)
        params.forEach((key, value) -> {
            query.setParameter(key, value);
            countQuery.setParameter(key, value);
        });

        // ✅ Paginação
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        Map<Long, PedidoResumoRecord> pedidosMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            BigDecimal valorTotal = (BigDecimal) row[0];
            Long pedidoId = ((Number) row[1]).longValue();
            String usuario = (String) row[2];
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
                            usuario,
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