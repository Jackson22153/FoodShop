package com.phucx.order.service.order;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.model.InvoiceDetails;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderSummary;
import com.phucx.order.model.OrderWithProducts;

public interface OrderService {
    // check pending order
    public Boolean isPendingOrder(String orderID);
    // update order status
    public Boolean updateOrderStatus(String orderID, OrderStatus status);
    // save order of customer
    public String saveFullOrder(OrderWithProducts order) throws JsonProcessingException;
    // validate customer's order
    public Boolean validateOrder(OrderWithProducts order) throws InvalidDiscountException, JsonProcessingException;
    // validate order's products with product's in stocks and update it by message queue
    public Boolean validateAndProcessOrder(OrderWithProducts order) throws InvalidDiscountException, JsonProcessingException;
    // update 
    public Boolean updateOrderEmployee(String orderID, String employeeID);
    // get order
    public OrderDetails getOrder(String orderID, OrderStatus status) throws JsonProcessingException;
    public OrderDetails getOrder(String orderID) throws JsonProcessingException;
    public Page<OrderDetails> getOrders(OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public OrderWithProducts getPendingOrderDetail(String orderID) throws JsonProcessingException;
    // get customer's order
    public Page<OrderDetails> getOrdersByCustomerID(String customerID, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public Page<OrderDetails> getOrdersByCustomerID(String customerID, OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public InvoiceDetails getInvoiceByCustomerID(String customerID, String orderID) throws JsonProcessingException;
    // get employee's order
    public Page<OrderDetails> getOrdersByEmployeeID(String employeeID, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public Page<OrderDetails> getOrdersByEmployeeID(String employeeID, OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID) throws JsonProcessingException;
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID, OrderStatus orderStatus) throws JsonProcessingException;
    // count order by status
    public OrderSummary getOrderSummary();
} 
