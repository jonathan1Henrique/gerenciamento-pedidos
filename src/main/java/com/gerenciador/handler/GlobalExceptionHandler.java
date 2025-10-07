package com.gerenciador.handler;


import com.gerenciador.exception.NotFoundException;
import com.gerenciador.exception.OrderNotBelongException;
import com.gerenciador.exception.OutOfStockOrderException;
import com.gerenciador.exception.InvalidDateRangeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ErroResponse> handleUnprocessableEntityException(InvalidDateRangeException ex) {
        ErroResponse erro = new ErroResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErroResponse> handleResourceNotFoundException(NotFoundException ex) {
        ErroResponse erro = new ErroResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErroResponse erroResponse = new ErroResponse();
        erroResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        erroResponse.setErros(new HashMap<>());
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            erroResponse.getErros().put(erro.getField(), erro.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResponse);
    }

    @ExceptionHandler(OutOfStockOrderException.class)
    public ResponseEntity<ErroResponse> handleOutOfStockOrderException(OutOfStockOrderException ex) {
        ErroResponse erro = new ErroResponse(HttpStatus.CONFLICT.value(), ex.getErros());
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> handleIllegalArgumentExceptionException(IllegalArgumentException ex) {
        ErroResponse erro = new ErroResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(OrderNotBelongException.class)
    public ResponseEntity<ErroResponse> handleIllegalArgumentExceptionException(OrderNotBelongException ex) {
        ErroResponse erro = new ErroResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.FORBIDDEN);
    }

}
