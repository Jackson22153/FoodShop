package com.phucx.account.service.order;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.model.InvoiceDetails;
import com.phucx.account.model.OrderDetails;
import com.phucx.account.model.OrderWithProducts;

public interface OrderService {
    // customer's orders
    public Page<OrderDetails> getCustomerOrders(String customerID, OrderStatus orderStatus, int pageNumber, int pageSize) throws JsonProcessingException;
    // customer's invoice
    public InvoiceDetails getCustomerInvoice(String orderID, String customerID) throws JsonProcessingException;
    
    // employee's order
    public OrderWithProducts getEmployeeOrder(String orderID, String employeeID) throws JsonProcessingException;
    // employee's orders
    public Page<OrderDetails> getEmployeeOrders(String employeeID, OrderStatus status, int pageNumber, int pageSize) throws JsonProcessingException;
} 
