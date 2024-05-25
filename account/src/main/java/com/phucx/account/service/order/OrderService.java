package com.phucx.account.service.order;

import org.springframework.data.domain.Page;

import com.phucx.account.constant.OrderStatus;
import com.phucx.account.model.InvoiceDTO;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;

public interface OrderService {
    // customer's orders
    public Page<OrderDetailsDTO> getCustomerOrders(String customerID, OrderStatus orderStatus, int pageNumber, int pageSize);
    // customer's invoice
    public InvoiceDTO getCustomerInvoice(String orderID, String customerID);
    
    // employee's order
    public OrderWithProducts getEmployeeOrder(String orderID, String employeeID);
    // employee's orders
    public Page<OrderDetailsDTO> getEmployeeOrders(String employeeID, OrderStatus status, int pageNumber, int pageSize);
} 
