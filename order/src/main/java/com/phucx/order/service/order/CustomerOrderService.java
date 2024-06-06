package com.phucx.order.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderWithProducts;

public interface CustomerOrderService {
    // place an order by customer
    public OrderDetails placeOrder(OrderWithProducts order, String userID) 
        throws JsonProcessingException, InvalidDiscountException, InvalidOrderException;
    public void receiveOrder(OrderWithProducts order) throws JsonProcessingException;
    
}