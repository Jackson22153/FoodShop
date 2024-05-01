package com.phucx.account.compositeKey;

import com.phucx.account.model.Order;
import com.phucx.account.model.Product;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable @Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsKey {
    @ManyToOne
    @JoinColumn(name = "ProductID")
    private Product product;
    
    @ManyToOne
    @JoinColumn(name = "OrderID")
    private Order order;
}
