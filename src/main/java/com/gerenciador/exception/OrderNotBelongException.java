package com.gerenciador.exception;

public class OrderNotBelongException extends RuntimeException {
    public OrderNotBelongException(String message) {
        super(message);
    }
}
