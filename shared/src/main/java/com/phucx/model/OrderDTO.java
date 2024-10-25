package com.phucx.model;

import com.phucx.constant.OrderStatus;

public class OrderDTO extends DataDTO{
    private String orderID;
    private String customerID;
    private String employeeID;
    
    private OrderStatus orderStatus;
    
    public OrderDTO() {
    }
    public OrderDTO(String orderID) {
        this.orderID = orderID;
    }
    public OrderDTO(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getOrderID() {
        return orderID;
    }
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
    public String getCustomerID() {
        return customerID;
    }
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
    public String getEmployeeID() {
        return employeeID;
    }
    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
