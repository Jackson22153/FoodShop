package com.phucx.shop.controller;

import javax.naming.InsufficientResourcesException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<String> notFoundExceptionHandler(NotFoundException exception){
        log.warn(exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InsufficientResourcesException.class)
    protected ResponseEntity<String> insufficientResourcesExceptionHandler(InsufficientResourcesException exception){
        log.warn(exception.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
