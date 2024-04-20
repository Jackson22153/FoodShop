package com.phucx.account.compositeKey;

import com.phucx.account.model.Discount;
import com.phucx.account.model.OrderDetails;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable @Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDiscountsID {
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "ProductID", referencedColumnName = "ProductID"),
        @JoinColumn(name = "OrderID", referencedColumnName = "OrderID")
    })
    private OrderDetails orderDetail;
    @ManyToOne
    @JoinColumn(name = "DiscountID", referencedColumnName = "DiscountID")
    private Discount discount;
}
