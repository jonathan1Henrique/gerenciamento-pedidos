package com.gerenciador.pedido.service;

import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.pedido.record.ItemRecord;
import com.gerenciador.pedido.record.PagamentoRecord;
import com.gerenciador.pedido.record.PedidoRecord;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoService {
    PedidoRecord salvar(List<ItemRecord> record);

    PedidoRecord editar(Long idPedido, List<ItemRecord> record);

    PageRecord listar(String usuario, String status, LocalDateTime dataInicioPedido, LocalDateTime dataFimPedido, LocalDateTime dataInicioPagamento, LocalDateTime dataFimPagamento, Pageable pageable);

    PedidoRecord pagamento(Long id, PagamentoRecord record);
}
