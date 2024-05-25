package com.phucx.account.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.phucx.account.constant.OrderStatus;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class OrderDetailsDTO {
    private String orderID;
    private List<ProductDTO> products;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private String employeeID;
    private String customerID;
    private String contactName;
    private String picture;

    public OrderDetailsDTO(String orderID, BigDecimal totalPrice, String customerID, String contactName, String picture, OrderStatus status) {
        this();
        this.orderID = orderID;
        this.totalPrice = totalPrice;
        this.customerID = customerID;
        this.contactName = contactName;
        this.picture = picture;
        this.status = status;
    }
    public OrderDetailsDTO(String orderID, List<ProductDTO> products, BigDecimal totalPrice, String customerID, String contactName, String picture, OrderStatus status) {
        this.orderID = orderID;
        this.products = products;
        this.totalPrice = totalPrice;
        this.customerID = customerID;
        this.contactName = contactName;
        this.picture = picture;
        this.status = status;
    }
    public OrderDetailsDTO() {
        this.products = new ArrayList<>();
        this.totalPrice = BigDecimal.valueOf(0);
    }
}
