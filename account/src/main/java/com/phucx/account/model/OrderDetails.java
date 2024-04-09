package com.phucx.account.model;

import com.phucx.account.compositeKey.OrderDetailsKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @Entity @ToString
// @IdClass(OrderDetailsKey.class)
@Table(name = "Order Details")
public class OrderDetails {
    @EmbeddedId
    private OrderDetailsKey key;
    
    @Column(name = "UnitPrice")
    private double unitPrice;
    @Column(name = "Quantity")
    private Integer quantity;
    @Column(name = "Discount")
    private float discount;
    public OrderDetails() {
    }
    public OrderDetails(OrderDetailsKey key, double unitPrice, Integer quantity, float discount) {
        this.key = key;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.discount = discount;
    }
}
