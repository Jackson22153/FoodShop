package com.phucx.account.exception;


public class CustomerNotFoundException extends UserNotFoundException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
    
}
