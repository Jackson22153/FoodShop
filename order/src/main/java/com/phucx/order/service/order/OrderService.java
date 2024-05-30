package com.phucx.order.service.order;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.model.InvoiceDTO;
import com.phucx.order.model.OrderDetailDTO;
import com.phucx.order.model.OrderWithProducts;

public interface OrderService {
    public Boolean isPendingOrder(String orderID);
    public Boolean updateOrderStatus(String orderID, OrderStatus status);
    // save order of customer
    public String saveFullOrder(OrderWithProducts order) throws JsonProcessingException;
    // validate customer's order
    public Boolean validateOrder(OrderWithProducts order) throws InvalidDiscountException, JsonProcessingException;
    // validate order's products with product's in stocks and update it
    public Boolean validateAndProcessOrder(OrderWithProducts order) throws InvalidDiscountException, JsonProcessingException;
    // update 
    public Boolean updateOrderEmployee(String orderID, String employeeID);
    // get order
    public OrderDetailDTO getOrder(String orderID, OrderStatus status) throws JsonProcessingException;
    public OrderDetailDTO getOrder(String orderID) throws JsonProcessingException;
    public OrderWithProducts getPendingOrderDetail(String orderID) throws JsonProcessingException;
    // get customer's order
    public Page<OrderDetailDTO> getOrdersByCustomerID(String customerID, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public InvoiceDTO getInvoiceByCustomerID(String customerID, String orderID) throws JsonProcessingException;
    // get employee's order
    public Page<OrderDetailDTO> getOrdersByEmployeeID(String employeeID, OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID) throws JsonProcessingException;


    
} 
