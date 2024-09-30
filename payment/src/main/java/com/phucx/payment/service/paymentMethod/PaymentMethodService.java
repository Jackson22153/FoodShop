package com.phucx.payment.service.paymentMethod;

import java.util.List;

import com.phucx.payment.exception.PaymentNotFoundException;
import com.phucx.payment.model.PaymentMethod;

public interface PaymentMethodService {
    public List<PaymentMethod> getPaymmentMethods();   
    public PaymentMethod getPaymentMethodByPaymentID(String paymentID) throws PaymentNotFoundException;
    public PaymentMethod getPaymentMethodByOrderID(String orderID) throws PaymentNotFoundException;
}
