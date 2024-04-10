package com.phucx.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.phucx.account.exception.InvalidDiscountException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler{

    @ExceptionHandler(value = InvalidDiscountException.class)
    protected ResponseEntity<String> handleInvalidDiscountException(){
        String message = "Invalid discount";
        return ResponseEntity.badRequest().body(message);
    }
}
