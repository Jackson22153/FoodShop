package com.phucx.account.exception;

import lombok.ToString;

@ToString
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message){
        super(message);
    }
}
