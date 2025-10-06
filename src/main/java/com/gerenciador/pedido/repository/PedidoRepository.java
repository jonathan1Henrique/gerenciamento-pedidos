package com.gerenciador.pedido.repository;

import com.gerenciador.pedido.entity.Pedido;
import com.gerenciador.pedido.repository.custom.PedidoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PedidoRepository extends JpaRepository<Pedido, Long>, PedidoRepositoryCustom {

}
