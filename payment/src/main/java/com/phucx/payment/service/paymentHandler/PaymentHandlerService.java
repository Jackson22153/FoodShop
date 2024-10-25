package com.phucx.payment.service.paymentHandler;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaymentHandlerService {
    public void paymentSuccessful(String orderId) throws JsonProcessingException;
    public void paymentFailed(String orderId) throws JsonProcessingException;
}
