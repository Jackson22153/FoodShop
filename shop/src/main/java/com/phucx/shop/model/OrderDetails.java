package com.phucx.shop.model;

import com.phucx.shop.compositeKey.OrderDetailsKey;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data @Entity @ToString
@Table(name = "Order Details")
public class OrderDetails {
    @EmbeddedId
    private OrderDetailsKey id;

    @ManyToOne
    @JoinColumn(name = "ProductID", referencedColumnName = "ProductID", insertable=false, updatable=false)
    private Products products;

    @ManyToOne
    @JoinColumn(name = "OrderID", referencedColumnName = "OrderID", insertable=false, updatable=false)
    private Orders orders;

    @Column(name = "UnitPrice", nullable = false)
    private double unitPrice;
    @Column(name = "Quantity", nullable = false)
    private Integer quantity;
    @Column(name = "Discount", nullable = false)
    private float discount;
}
