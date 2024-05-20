package com.phucx.order.service.order;

import java.sql.SQLException;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.OrderDetailsDTO;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Order;

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
    public OrderWithProducts getPendingOrderDetail(int orderID) throws InvalidOrderException;
} 
