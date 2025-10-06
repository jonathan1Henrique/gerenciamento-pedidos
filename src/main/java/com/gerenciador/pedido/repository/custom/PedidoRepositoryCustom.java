package com.gerenciador.pedido.repository.custom;

import com.gerenciador.pedido.record.PedidoResumoRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PedidoRepositoryCustom {
    Page<PedidoResumoRecord> buscarPedidosComProdutos(
            String usuario,
            String status,
            LocalDateTime dataInicioPedido,
            LocalDateTime dataFimPedido,
            LocalDateTime dataInicioPagamento,
            LocalDateTime dataFimPagamento,
            Pageable pageable
    );
}
