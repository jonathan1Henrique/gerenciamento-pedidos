package com.gerenciador.pedido.repository;

import com.gerenciador.pedido.entity.PedidoItens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItens,  Long> {

    List<PedidoItens> findByIdPedido(Long idPedido);
}
