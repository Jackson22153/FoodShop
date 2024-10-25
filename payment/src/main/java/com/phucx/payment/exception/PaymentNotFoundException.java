package com.phucx.payment.exception;

public class PaymentNotFoundException extends NotFoundException {
    public PaymentNotFoundException(String message){
        super(message);
    }
}
