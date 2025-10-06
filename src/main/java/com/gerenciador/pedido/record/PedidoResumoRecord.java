package com.gerenciador.pedido.record;

import com.gerenciador.pedido.enums.StatusPedido;
import com.gerenciador.produto.record.ProdutoRecord;

import java.math.BigDecimal;
import java.util.List;

public record PedidoResumoRecord(
        BigDecimal valorTotal,
        Long pedidoId,
        String usuario,
        StatusPedido status,
        List<ProdutoRecord> produtos,
        Integer quantidadeProdutos
) {
}
