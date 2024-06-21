package com.phucx.order.exception;

public class InSufficientInventoryException extends Exception {
    public InSufficientInventoryException(String message){
        super(message);
    }
}
