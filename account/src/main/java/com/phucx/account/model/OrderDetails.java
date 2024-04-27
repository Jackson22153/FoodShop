package com.phucx.account.model;

import java.math.BigDecimal;

import com.phucx.account.compositeKey.OrderDetailsKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data @Entity @ToString
@Table(name = "Order Details")
public class OrderDetails {
    @EmbeddedId
    private OrderDetailsKey key;
    
    @Column(name = "UnitPrice")
    private BigDecimal unitPrice;
    @Column(name = "Quantity")
    private Integer quantity;

    public OrderDetails() {
    }
    public OrderDetails(OrderDetailsKey key, BigDecimal unitPrice, Integer quantity) {
        this.key = key;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }
}
