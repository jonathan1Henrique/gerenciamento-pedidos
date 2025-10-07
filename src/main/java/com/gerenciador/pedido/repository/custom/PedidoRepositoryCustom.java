package com.gerenciador.pedido.repository.custom;

import com.gerenciador.dashboard.record.FaturamentoMensalRecord;
import com.gerenciador.dashboard.record.TicketMedioRecord;
import com.gerenciador.dashboard.record.TopCincoRecord;
import com.gerenciador.pedido.record.PedidoResumoRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepositoryCustom {
    Page<PedidoResumoRecord> buscarPedidosComProdutos(
            String usuario,
            String status,
            Pageable pageable
    );

    List<TopCincoRecord> buscarTopCincoUsuarios();

    List<TicketMedioRecord> buscarTicketMedioPorUsuario();

    FaturamentoMensalRecord buscarFaturamentoMensal(LocalDate startDate, LocalDate endDate);
}
