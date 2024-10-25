package com.phucx.payment.service.payment;

import com.paypal.base.rest.PayPalRESTException;
import com.phucx.model.PaymentDTO;
import com.phucx.payment.model.PaymentResponse;

public interface PaymentProcessorService {
    public PaymentResponse createPayment(PaymentDTO payment) throws PayPalRESTException;
}
