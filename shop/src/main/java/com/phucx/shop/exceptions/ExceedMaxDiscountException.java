package com.phucx.shop.exceptions;

public class ExceedMaxDiscountException extends Exception{
    public ExceedMaxDiscountException(String message){
        super(message);
    }
}
