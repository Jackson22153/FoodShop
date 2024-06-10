package com.phucx.order.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
public class OrderItem {
    private Integer productID;
    private String productName;
    private Integer quantity;
    private List<OrderItemDiscount> discounts;
    private String picture;
    private BigDecimal unitPrice;
    private BigDecimal extendedPrice;


    public OrderItem(Integer productID, String productName, Integer quantity, String picture, BigDecimal unitPrice) {
        this.productName=productName;
        this.productID = productID;
        this.quantity = quantity;
        this.picture = picture;
        this.unitPrice = unitPrice;
    }

    public OrderItem() {
        this.discounts = new ArrayList<>();
        this.extendedPrice = BigDecimal.valueOf(0);
    }
}
