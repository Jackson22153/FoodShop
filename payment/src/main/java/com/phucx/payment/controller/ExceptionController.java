package com.phucx.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.phucx.payment.exception.NotFoundException;
import com.phucx.payment.model.ResponseFormat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler{
    
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleNotFoundException(NotFoundException notFoundException){
        log.error("Error: {}", notFoundException.getMessage());
        return ResponseEntity.notFound().build();
    }
}
