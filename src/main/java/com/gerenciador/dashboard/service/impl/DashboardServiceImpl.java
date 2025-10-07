package com.gerenciador.dashboard.service.impl;

import com.gerenciador.dashboard.record.FaturamentoMensalRecord;
import com.gerenciador.dashboard.record.TicketMedioRecord;
import com.gerenciador.dashboard.record.TopCincoRecord;
import com.gerenciador.dashboard.service.DashboardService;
import com.gerenciador.pedido.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final PedidoService pedidoService;

    @Override
    public List<TopCincoRecord> topCinco() {
        return pedidoService.topCinco();
    }

    @Override
    public List<TicketMedioRecord> buscaTicketMedio() {
        return pedidoService.buscarTicketMedioPorUsuario();
    }

    @Override
    public FaturamentoMensalRecord buscarFaturamentoMensal() {
        return pedidoService.buscarFaturamentoMensal();
    }
}
