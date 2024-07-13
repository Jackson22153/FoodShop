package com.phucx.keycloakmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.keycloakmanagement.exception.NotFoundException;
import com.phucx.keycloakmanagement.model.ResponseFormat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler{
    
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ResponseFormat> handleJsonProcessException(JsonProcessingException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setError(exception.getMessage());
        responseFormat.setStatus(false);
        return ResponseEntity.internalServerError().body(responseFormat);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseFormat> handleNotFoundException(NotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        return ResponseEntity.notFound().build(); 
    }

}
