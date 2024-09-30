package com.phucx.payment.service.paymentHandler;

import com.phucx.payment.model.Payment;
import com.phucx.payment.model.PaymentDTO;

public interface CODHandlerService {
    public Payment createPayment(PaymentDTO paymentDTO);
    public Boolean paymentSuccessfully(String paymentID);
    public Boolean paymentCancelled(String paymentID);
}
