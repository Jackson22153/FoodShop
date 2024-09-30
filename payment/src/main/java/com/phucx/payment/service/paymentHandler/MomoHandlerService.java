package com.phucx.payment.service.paymentHandler;

import com.phucx.payment.model.PaymentDTO;

public interface MomoHandlerService {
    public String createPayment(PaymentDTO paymentDTO);
    public Boolean paymentSuccessfully(String orderID);
}
