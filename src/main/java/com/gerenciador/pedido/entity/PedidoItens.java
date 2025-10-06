package com.gerenciador.pedido.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@Entity
@Table(name = "pedido_itens")
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idPedido;
    private Long idProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
}
