package com.phucx.account.compositeKey;

import com.phucx.account.model.Orders;
import com.phucx.account.model.Products;

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
    private Products productID;
    
    @ManyToOne
    @JoinColumn(name = "OrderID")
    private Orders orderID;
}
