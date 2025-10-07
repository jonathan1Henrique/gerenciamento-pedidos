package com.gerenciador.dashboard.service;

import com.gerenciador.dashboard.record.FaturamentoMensalRecord;
import com.gerenciador.dashboard.record.TicketMedioRecord;
import com.gerenciador.dashboard.record.TopCincoRecord;

import java.util.List;


public interface DashboardService {
    List<TopCincoRecord> topCinco();
    List<TicketMedioRecord> buscaTicketMedio();
    FaturamentoMensalRecord buscarFaturamentoMensal();
}
