package com.phucx.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Integer productID;
    private Integer quantity;
    private Float discount;
}
