package com.phucx.payment.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.payment.model.OrderDetails;

public interface OrderService {
    public void updateOrderStatusAsCanceled(String orderID) throws JsonProcessingException;
    public OrderDetails getOrder(String orderID) throws JsonProcessingException;
}
