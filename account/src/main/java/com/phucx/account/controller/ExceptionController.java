package com.phucx.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler{

    @ExceptionHandler(value = InvalidDiscountException.class)
    protected ResponseEntity<String> handleInvalidDiscountException(InvalidDiscountException exception){
        String message = exception.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(value = InvalidOrderException.class)
    protected ResponseEntity<String> handleInvalidOrderException(InvalidOrderException exception){
        String message = exception.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception){
        String message = exception.getMessage();
        return ResponseEntity.badRequest().body(message);
    }
}
