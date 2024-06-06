package com.phucx.order.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.OrderWithProducts;

public interface EmployeeOrderService {
    // processing order of customer
    public void confirmOrder(OrderWithProducts order, String userID) throws InvalidOrderException, JsonProcessingException;
    public void cancelOrder(OrderWithProducts order, String userID) throws JsonProcessingException;
    public void fulfillOrder(OrderWithProducts order) throws JsonProcessingException;
    
}
