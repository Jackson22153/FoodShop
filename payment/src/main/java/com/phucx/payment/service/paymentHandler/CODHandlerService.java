package com.phucx.payment.service.paymentHandler;

import com.phucx.model.PaymentDTO;
import com.phucx.payment.model.Payment;

public interface CODHandlerService {
    public Payment createPayment(PaymentDTO paymentDTO);
    public Boolean paymentSuccessfully(String paymentID);
    public Boolean paymentCancelled(String paymentID);
}
