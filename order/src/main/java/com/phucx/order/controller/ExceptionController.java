package com.phucx.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.ResponseFormat;

import jakarta.persistence.EntityExistsException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler{

    @ExceptionHandler(value = InvalidDiscountException.class)
    protected ResponseEntity<ResponseFormat> handleInvalidDiscountException(InvalidDiscountException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = InvalidOrderException.class)
    protected ResponseEntity<ResponseFormat> handleInvalidOrderException(InvalidOrderException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<ResponseFormat> handleIllegalArgumentException(IllegalArgumentException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = NullPointerException.class)
    protected ResponseEntity<ResponseFormat> handleNullPointerException(NullPointerException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleNotFoundException(NotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = EntityExistsException.class)
    protected ResponseEntity<ResponseFormat> handleEntityExistsException(EntityExistsException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<ResponseFormat> handleRuntimeException(RuntimeException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
