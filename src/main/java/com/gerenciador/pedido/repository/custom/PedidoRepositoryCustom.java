package com.gerenciador.pedido.repository.custom;

import com.gerenciador.pedido.record.PedidoResumoRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoRepositoryCustom {
    Page<PedidoResumoRecord> buscarPedidosComProdutos(
            String usuario,
            String status,
            Pageable pageable
    );
}
