package com.gerenciador.pedido.record;

import com.gerenciador.pedido.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoRecord (
        Long id,
        String usuario,
        StatusPedido statusPedido,
        BigDecimal valorTotal,
        LocalDateTime dataPedido,
        LocalDateTime dataPagamento
){}
