package com.phucx.shop.exceptions;

public class EmptyCartException extends Exception{
    public EmptyCartException(String message){
        super(message);
    }
}
