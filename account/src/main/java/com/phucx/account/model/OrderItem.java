package com.phucx.account.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class OrderItem {
    private Integer productID;
    private String productName;
    private Integer quantity;
    private List<OrderItemDiscount> discounts;
    private String picture;
    private Double unitPrice;
    private Double extendedPrice;



    public OrderItem(Integer productID, String productName, Integer quantity, String picture, Double unitPrice) {
        this.productName=productName;
        this.productID = productID;
        this.quantity = quantity;
        this.picture = picture;
        this.unitPrice = unitPrice;
    }



    public OrderItem(Integer productID, String productName, Integer quantity, List<OrderItemDiscount> discounts, String picture,
            Double unitPrice, Double extendedPrice) {
        this.productName = productName;
        this.productID = productID;
        this.quantity = quantity;
        this.discounts = discounts;
        this.picture = picture;
        this.unitPrice = unitPrice;
        this.extendedPrice = extendedPrice;
    }



    public OrderItem() {
        this.discounts = new ArrayList<>();
        this.extendedPrice = Double.valueOf(0);
    }
}
