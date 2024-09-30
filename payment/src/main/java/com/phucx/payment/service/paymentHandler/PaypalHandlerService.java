package com.phucx.payment.service.paymentHandler;

import com.paypal.api.payments.Payment;
import com.phucx.payment.model.PaymentDTO;

public interface PaypalHandlerService {
    public Payment createPayment(PaymentDTO paymentDTO, String cancelUrl, String successUrl);
    public Payment executePayment(String paymentID, String payerID);
    public Boolean paymentSuccessfully(String paymentID, String payerID);
    public Boolean paymentCancelled(String orderId);
}
