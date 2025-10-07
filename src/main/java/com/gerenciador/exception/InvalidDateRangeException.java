package com.gerenciador.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidDateRangeException extends RuntimeException {

    public InvalidDateRangeException(String message) {
        super(message);

    }
}
