package com.phucx.payment.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShippingProduct {
    private Integer totalHeight;
    private Integer totalLength;
    private Integer totalWeight;
    private Integer totalWidth;
    private BigDecimal totalPrice;
}
