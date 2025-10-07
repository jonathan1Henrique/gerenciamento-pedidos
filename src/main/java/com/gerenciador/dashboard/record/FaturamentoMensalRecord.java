package com.gerenciador.dashboard.record;

import java.math.BigDecimal;

public record FaturamentoMensalRecord(Integer mes, Integer ano, BigDecimal valorTotalFaturado) {}
