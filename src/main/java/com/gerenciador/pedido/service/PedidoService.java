package com.gerenciador.pedido.service;

import com.gerenciador.dashboard.record.FaturamentoMensalRecord;
import com.gerenciador.dashboard.record.TicketMedioRecord;
import com.gerenciador.dashboard.record.TopCincoRecord;
import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.pedido.record.ItemRecord;
import com.gerenciador.pedido.record.PagamentoRecord;
import com.gerenciador.pedido.record.PedidoRecord;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PedidoService {
    PedidoRecord salvar(List<ItemRecord> record);

    PedidoRecord editar(Long idPedido, List<ItemRecord> record);

    PageRecord listar(String usuario, String status, Pageable pageable);

    PedidoRecord pagamento(Long id, PagamentoRecord record);

    List<TopCincoRecord> topCinco();

    List<TicketMedioRecord> buscarTicketMedioPorUsuario();

    FaturamentoMensalRecord buscarFaturamentoMensal(LocalDate startDate, LocalDate endDate);
}
