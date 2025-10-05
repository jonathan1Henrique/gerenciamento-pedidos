package com.gerenciador.produto.record;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;


public record ProdutoRecord(

    Long id,

    @NotBlank @Size(min = 3, max = 100)
    String nome,

    @NotBlank
    String descricao,

    @NotNull(message = "O valor é obrigatório.")
    @Positive(message = "O valor deve ser positivo.")
    BigDecimal preco,

    @Positive(message = "O valor deve ser positivo.")
    Long idCategoria,

    @NotNull(message = "O valor é obrigatório.")
    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    Integer quantidadeEstoque
) {}
