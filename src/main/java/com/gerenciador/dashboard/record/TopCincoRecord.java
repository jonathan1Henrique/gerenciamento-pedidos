package com.gerenciador.dashboard.record;

import java.math.BigDecimal;


public record TopCincoRecord(String usuario, Long totalPedidos, BigDecimal totalGasto) {
}
