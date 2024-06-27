package com.phucx.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.phucx.account.exception.CustomerNotFoundException;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.InvalidUserException;
import com.phucx.account.exception.NotFoundException;
import com.phucx.account.exception.RoleNotFoundException;
import com.phucx.account.exception.ShipperNotFoundException;
import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.ResponseFormat;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler{

    @ExceptionHandler(value = InvalidUserException.class)
    protected ResponseEntity<ResponseFormat> handleInvalidUserException(InvalidUserException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleUserNotFoundException(UserNotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = CustomerNotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleCustomerNotFoundException(CustomerNotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = EmployeeNotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleEmployeeNotFoundException(EmployeeNotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = ShipperNotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleShipperNotFoundException(ShipperNotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleRoleNotFoundException(RoleNotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.notFound().build();
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
        return ResponseEntity.notFound().build();
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
