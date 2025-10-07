package com.gerenciador.dashboard.controller;

import com.gerenciador.dashboard.record.FaturamentoMensalRecord;
import com.gerenciador.dashboard.record.TicketMedioRecord;
import com.gerenciador.dashboard.record.TopCincoRecord;
import com.gerenciador.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Operações relacionadas a dashboard")
public class DashboardController {

    private final DashboardService service;

    @GetMapping("/top-cinco")
    @Operation(summary = "Top 5 usuários que mais compraram")
    public ResponseEntity<List<TopCincoRecord>> topCinco() {

        return ResponseEntity.ok(service.topCinco());
    }

    @GetMapping("/ticket-medio")
    @Operation(summary = "Ticket médio dos pedidos de cada usuário.")
    public ResponseEntity<List<TicketMedioRecord>> ticketMedio() {
        return ResponseEntity.ok(service.buscaTicketMedio());
    }

    @GetMapping("/valor-faturado")
    @Operation(summary = "Valor total faturado no mês.")
    public ResponseEntity<FaturamentoMensalRecord> valorFaturado(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return ResponseEntity.ok(service.buscarFaturamentoMensal(startDate, endDate));
    }
}
