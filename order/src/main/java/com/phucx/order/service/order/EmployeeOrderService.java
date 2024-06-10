package com.phucx.order.service.order;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderWithProducts;

public interface EmployeeOrderService {
    // processing order of customer
    public void confirmOrder(String orderID, String userID) throws InvalidOrderException, JsonProcessingException;
    public void cancelOrder(OrderWithProducts order, String userID) throws JsonProcessingException;
    public void fulfillOrder(OrderWithProducts order) throws JsonProcessingException;
    // get orders
    public Page<OrderDetails> getOrders(String userID, OrderStatus status, int pageNumber, int pageSize) throws JsonProcessingException;
    // get order
    public OrderWithProducts getOrder(String orderID, String userID, OrderStatus status) throws JsonProcessingException;
    public OrderWithProducts getPendingOrder(String orderID, String userID) throws JsonProcessingException;
}
