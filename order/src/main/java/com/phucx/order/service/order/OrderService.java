package com.phucx.order.service.order;

import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.model.OrderDetailDTO;
import com.phucx.order.model.OrderWithProducts;

public interface OrderService {
    public Boolean isPendingOrder(String orderID);
    public boolean updateOrderStatus(String orderID, OrderStatus status);
    // save order of customer
    public String saveFullOrder(OrderWithProducts order);
    // validate customer's order
    public boolean validateOrder(OrderWithProducts order) throws InvalidDiscountException;
    // validate order's products with product's in stocks and update it
    public boolean validateAndProcessOrder(OrderWithProducts order) throws InvalidDiscountException;
    // update 
    public boolean updateOrderEmployee(String orderID, String employeeID);
    // get order
    public OrderDetailDTO getOrder(String orderID, OrderStatus status);
    public OrderDetailDTO getOrder(String orderID);
    public OrderWithProducts getPendingOrderDetail(String orderID);
    
} 
