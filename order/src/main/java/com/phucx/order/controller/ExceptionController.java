package com.phucx.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;

import jakarta.persistence.EntityExistsException;
import jakarta.ws.rs.NotFoundException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler{

    @ExceptionHandler(value = InvalidDiscountException.class)
    protected ResponseEntity<String> handleInvalidDiscountException(InvalidDiscountException exception){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = InvalidOrderException.class)
    protected ResponseEntity<String> handleInvalidOrderException(InvalidOrderException exception){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = NullPointerException.class)
    protected ResponseEntity<String> handleNullPointerException(NullPointerException exception){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<String> handleNotFoundException(NotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = EntityExistsException.class)
    protected ResponseEntity<String> handleEntityExistsException(EntityExistsException exception){
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<String> handleRuntimeException(RuntimeException exception){
        return ResponseEntity.badRequest().build();
    }
}
