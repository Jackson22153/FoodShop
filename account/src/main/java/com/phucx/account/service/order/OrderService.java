package com.phucx.account.service.order;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.account.constraint.OrderStatus;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.Orders;

public interface OrderService {
    public boolean updateOrderStatus(Integer orderID, OrderStatus status);
    public Orders saveOrder(OrderWithProducts order);
    // validate order's products with product's in stocks and update it
    public boolean validateOrder(OrderWithProducts order) throws RuntimeException;
    public boolean updateOrderEmployee(Integer orderID, String employeeID);
    public Page<OrderWithProducts> getPendingOrders(int pageNumber, int pageSize);
} 
