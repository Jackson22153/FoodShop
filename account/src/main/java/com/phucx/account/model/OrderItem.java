package com.phucx.account.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class OrderItem {
    private Integer productID;
    private Integer quantity;
    private List<OrderItemDiscount> discounts;
    public OrderItem(Integer productID, Integer quantity) {
        this();
        this.productID = productID;
        this.quantity = quantity;
    }
    public OrderItem(Integer productID, Integer quantity, List<OrderItemDiscount> discounts) {
        this.productID = productID;
        this.quantity = quantity;
        this.discounts = discounts;
    }
    public OrderItem() {
        this.discounts = new ArrayList<>();
    }
}
