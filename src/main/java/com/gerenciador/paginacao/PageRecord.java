package com.gerenciador.paginacao;


import java.util.List;

public record PageRecord(
        List content,
        Integer totalElements,
        Integer totalPages,
        Integer size,
        Integer number,
        Boolean first,
        Boolean last,
        Integer numberOfElements,
        Boolean empty
) {
}
