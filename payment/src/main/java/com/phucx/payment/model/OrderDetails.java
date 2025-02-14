package com.phucx.payment.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.phucx.payment.constant.OrderStatus;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class OrderDetails {
    private String orderID;
    private List<OrderProduct> products;
    private BigDecimal totalPrice;
    private BigDecimal freight;
    private OrderStatus status;
    private String employeeID;
    private String customerID;
    private String contactName;
    private String picture;

    public OrderDetails(String orderID, BigDecimal totalPrice, String customerID, String contactName, String picture, OrderStatus status) {
        this();
        this.orderID = orderID;
        this.totalPrice = totalPrice;
        this.customerID = customerID;
        this.contactName = contactName;
        this.picture = picture;
        this.status = status;
    }
    public OrderDetails(String orderID, List<OrderProduct> products, BigDecimal totalPrice, String customerID, String contactName, String picture, OrderStatus status) {
        this.orderID = orderID;
        this.products = products;
        this.totalPrice = totalPrice;
        this.customerID = customerID;
        this.contactName = contactName;
        this.picture = picture;
        this.status = status;
    }
    public OrderDetails() {
        this.products = new ArrayList<>();
        this.totalPrice = BigDecimal.valueOf(0);
        this.totalPrice = BigDecimal.valueOf(0);
    }
}
