package com.gerenciador.pedido.repository;

import com.gerenciador.dashboard.record.FaturamentoMensalRecord;
import com.gerenciador.dashboard.record.TicketMedioRecord;
import com.gerenciador.dashboard.record.TopCincoRecord;
import com.gerenciador.pedido.entity.Pedido;
import com.gerenciador.pedido.repository.custom.PedidoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PedidoRepository extends JpaRepository<Pedido, Long>, PedidoRepositoryCustom {
    @Query(value = """
            SELECT 
                p.usuario AS usuario,
                COUNT(p.id) AS totalPedidos,
                SUM(p.valor_total) AS totalGasto
            FROM pedido p
            WHERE p.status = 'CONCLUIDO'
            GROUP BY p.usuario
            ORDER BY totalGasto DESC
            LIMIT 5
            """, nativeQuery = true)
    List<TopCincoRecord> buscarTopCincoUsuarios();

    @Query(value = """
            SELECT 
                p.usuario AS usuario,
                ROUND(SUM(p.valor_total) / COUNT(p.id), 2) AS ticketMedio
            FROM pedido p
            WHERE p.status = 'CONCLUIDO'
            GROUP BY p.usuario
            ORDER BY ticketMedio DESC
            """,
            nativeQuery = true)
    List<TicketMedioRecord> buscarTicketMedioPorUsuario();


    @Query(value = """
            SELECT 
                MONTH(p.data_pagamento) AS mes,
                YEAR(p.data_pagamento) AS ano,
                SUM(p.valor_total) AS valorTotalFaturado
            FROM pedido p
            WHERE p.status = 'CONCLUIDO'
              AND MONTH(p.data_pagamento) = MONTH(CURRENT_DATE())
              AND YEAR(p.data_pagamento) = YEAR(CURRENT_DATE())
            GROUP BY YEAR(p.data_pagamento), MONTH(p.data_pagamento)
            """,
            nativeQuery = true)
    FaturamentoMensalRecord buscarFaturamentoMensal();
}
