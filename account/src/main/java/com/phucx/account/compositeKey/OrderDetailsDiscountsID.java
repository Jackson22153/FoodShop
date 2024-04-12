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
        @JoinColumn(name = "productID"),
        @JoinColumn(name = "orderID")
    })
    private OrderDetails orderDetail;
    @ManyToOne
    @JoinColumn(name = "discountID")
    private Discount discount;
}
