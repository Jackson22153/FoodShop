package com.phucx.shop.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable @Data
public class OrderDetailsKey {
    private Integer productID;
    private Integer orderID;

}
