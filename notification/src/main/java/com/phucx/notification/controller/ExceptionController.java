package com.phucx.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

import javax.naming.NameNotFoundException;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler{
    
    @ExceptionHandler(NameNotFoundException.class)
    protected ResponseEntity<Void> handleNameNotFoundException(NameNotFoundException exception){
        log.warn("Error: {}", exception.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
