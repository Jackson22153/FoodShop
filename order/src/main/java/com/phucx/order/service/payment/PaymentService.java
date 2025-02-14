package com.phucx.order.service.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.model.PaymentDTO;
import com.phucx.order.model.PaymentDetails;
import com.phucx.order.model.PaymentResponse;

public interface PaymentService {
    public PaymentResponse createPayment(PaymentDTO paymentDTO) throws JsonProcessingException;
    public PaymentResponse updatePaymentByOrderIDAsSuccesful(String orderID) throws JsonProcessingException;
    public PaymentResponse updatePaymentByOrderIDAsCanceled(String orderID) throws JsonProcessingException;
    
    public PaymentDetails getPayment(String orderID) throws JsonProcessingException;

}
