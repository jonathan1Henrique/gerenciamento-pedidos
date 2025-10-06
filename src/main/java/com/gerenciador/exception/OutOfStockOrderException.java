package com.gerenciador.exception;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OutOfStockOrderException extends RuntimeException {

    List<String> erros;
    public OutOfStockOrderException(List<String> erros) {
        this.erros = erros;
    }
}
