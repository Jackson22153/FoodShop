package com.phucx.order.service.order;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.InvoiceDetails;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderSummary;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.ResponseFormat;

public interface OrderService {
    // check pending order
    public Boolean isPendingOrder(String orderID) throws NotFoundException;
    // update order status
    public Boolean updateOrderStatus(String orderID, OrderStatus status) throws NotFoundException;
    // save order of customer
    public String saveFullOrder(OrderWithProducts order) throws JsonProcessingException, NotFoundException;
    // validate customer's order
    public Boolean validateOrder(OrderWithProducts order) throws InvalidDiscountException, JsonProcessingException;
    // validate order's products with product's in stocks and update it by message queue
    public ResponseFormat validateAndProcessOrder(OrderWithProducts order) throws JsonProcessingException;
    // update employee id to order
    public Boolean updateOrderEmployee(String orderID, String employeeID);
    // get order
    public OrderDetails getOrder(String orderID, OrderStatus status) throws JsonProcessingException, NotFoundException;
    public OrderDetails getOrder(String orderID) throws JsonProcessingException, NotFoundException;
    public Page<OrderDetails> getOrders(OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException;
    public OrderWithProducts getPendingOrderDetail(String orderID) throws JsonProcessingException, NotFoundException;
    // get customer's order
    public Page<OrderDetails> getOrdersByCustomerID(String customerID, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException;
    public Page<OrderDetails> getOrdersByCustomerID(String customerID, OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException;
    public InvoiceDetails getInvoiceByCustomerID(String customerID, String orderID) throws JsonProcessingException, NotFoundException;
    // get employee's order
    public Page<OrderDetails> getOrdersByEmployeeID(String employeeID, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException;
    public Page<OrderDetails> getOrdersByEmployeeID(String employeeID, OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException;
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID) throws JsonProcessingException, NotFoundException;
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID, OrderStatus status) throws JsonProcessingException, NotFoundException;
    // count order by status
    public OrderSummary getOrderSummary();
} 
