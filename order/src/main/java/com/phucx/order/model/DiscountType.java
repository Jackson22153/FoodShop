package com.phucx.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiscountType {
    private Integer discountTypeID;
    private String discountType;
    private String description;
}
