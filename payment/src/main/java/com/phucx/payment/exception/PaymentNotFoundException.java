package com.phucx.payment.exception;

public class PaymentNotFoundException extends Exception {
    public PaymentNotFoundException(String message){
        super(message);
    }
}
