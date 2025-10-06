package com.gerenciador.pedido.record;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemRecord(
        Long id,

        @NotNull(message = "O valor é obrigatório.")
        @Min(value = 1, message = "A quantidade deve ser maior que zero.")
        Integer quantidade,

        @NotNull(message = "A lista de itens não pode ser nula.")
        Long idProduto
) {
}
