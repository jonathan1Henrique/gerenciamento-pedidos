package com.gerenciador.dashboard.service.impl;

import com.gerenciador.dashboard.record.FaturamentoMensalRecord;
import com.gerenciador.dashboard.record.TicketMedioRecord;
import com.gerenciador.dashboard.record.TopCincoRecord;
import com.gerenciador.dashboard.service.DashboardService;
import com.gerenciador.exception.InvalidDateRangeException;
import com.gerenciador.exception.NotFoundException;
import com.gerenciador.pedido.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public FaturamentoMensalRecord buscarFaturamentoMensal(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("A data inicial não pode ser maior que a data final.");
        }
        FaturamentoMensalRecord faturamentoMensalRecord = pedidoService.buscarFaturamentoMensal(startDate, endDate);
        if (faturamentoMensalRecord == null) {
            throw new NotFoundException("Não houve faturamento para o periodo informado");
        }
        return faturamentoMensalRecord;
    }
}
