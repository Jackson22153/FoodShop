package com.phucx.shop.exceptions;

public class InSufficientInventoryException extends Exception {
    public InSufficientInventoryException(String message){
        super(message);
    }
}
