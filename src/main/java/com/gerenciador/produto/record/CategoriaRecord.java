package com.gerenciador.produto.record;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRecord(
        Long id,

        @NotBlank @Size(min = 3, max = 100)
        String nome,

        @NotBlank
        String descricao
) {}
