package com.phucx.payment.service.paymentHandler;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.model.PaymentDTO;

public interface ZaloPayHandlerService {
    // create a new payment
    public String createPayment(PaymentDTO paymentDTO);
    // query payment from zalo
    public Map<String, Object> queryPayment(String transId);
    public Boolean executePayment(String orderId, String transId) throws JsonProcessingException;
    public String callback(String jsonStr);
}
