package com.phucx.account.service.order;

import java.sql.SQLException;
import org.springframework.data.domain.Page;

import com.phucx.account.constant.OrderStatus;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.InvoiceDTO;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.Order;

import jakarta.ws.rs.NotFoundException;

public interface OrderService {
    public boolean isPendingOrder(Integer orderID) throws InvalidOrderException;
    public boolean updateOrderStatus(Integer orderID, OrderStatus status);
    public Order saveFullOrder(OrderWithProducts order) 
        throws NotFoundException, SQLException, RuntimeException, InvalidDiscountException;
    public boolean validateOrder(OrderWithProducts order) throws InvalidDiscountException;
    // validate order's products with product's in stocks and update it
    public boolean validateAndProcessOrder(OrderWithProducts order) throws RuntimeException, InvalidDiscountException;
    // update 
    public boolean updateOrderEmployee(Integer orderID, String employeeID);

    public OrderDetailsDTO getOrder(Integer orderID, OrderStatus status);
    public OrderDetailsDTO getOrder(Integer orderID);

    // get order for employee
    public Page<OrderDetailsDTO> getEmployeeOrders(Integer pageNumber, Integer pageSize, String employeeID, OrderStatus status);
    public Page<OrderDetailsDTO> getEmployeeOrders(Integer pageNumber, Integer pageSize, String employeeID);
    public Page<OrderDetailsDTO> getPendingOrders(int pageNumber, int pageSize);
    public OrderWithProducts getEmployeeOrderDetail(int orderID, String employeeID) throws InvalidOrderException;
    public OrderWithProducts getPendingOrderDetail(int orderID) throws InvalidOrderException;
    // get order for customer
    public InvoiceDTO getCustomerInvoice(int orderID, String customerID) throws InvalidOrderException;
    public Page<OrderDetailsDTO> getCustomerOrders(Integer pageNumber, Integer pageSize, String customerID, OrderStatus orderStatus);
    public Page<OrderDetailsDTO> getCustomerOrders(Integer pageNumber, Integer pageSize, String customerID); 
} 
