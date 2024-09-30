package com.phucx.payment.service.paymentHandler;

import com.paypal.api.payments.Payment;
import com.phucx.payment.model.Order;

public interface PaymentHandlerService {
    public Payment createPayment(Order order, String cancelUrl, String successUrl);
    public Payment executePayment(String paymentID, String payerID);
}
