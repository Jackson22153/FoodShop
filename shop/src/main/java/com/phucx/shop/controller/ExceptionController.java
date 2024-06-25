package com.phucx.shop.controller;

import java.io.IOException;

import javax.naming.InsufficientResourcesException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.phucx.shop.exceptions.EmptyCartException;
import com.phucx.shop.exceptions.EntityExistsException;
import com.phucx.shop.exceptions.InvalidOrderException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.ResponseFormat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmptyCartException.class)
    protected ResponseEntity<ResponseFormat> handleEmptyCartException(EmptyCartException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError(exception.getMessage());
        response.setStatus(false);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(InvalidOrderException.class)
    protected ResponseEntity<ResponseFormat> handleInvalidOrderException(InvalidOrderException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError(exception.getMessage());
        response.setStatus(false);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ResponseFormat> handleRuntimeException(RuntimeException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError(exception.getMessage());
        response.setStatus(false);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ResponseFormat> handlerNotFoundException(NotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError(exception.getMessage());
        response.setStatus(false);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<ResponseFormat> handlerEntityExistsException(EntityExistsException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError(exception.getMessage());
        response.setStatus(false);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(InsufficientResourcesException.class)
    protected ResponseEntity<ResponseFormat> handlerInsufficientResourcesException(InsufficientResourcesException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError(exception.getMessage());
        response.setStatus(false);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ResponseFormat> handlerIOExceptionr(IOException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError("An Error has occurred");
        response.setStatus(false);
        return ResponseEntity.internalServerError().body(response);
    }
}
