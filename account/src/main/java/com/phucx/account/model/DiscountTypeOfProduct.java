package com.phucx.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class DiscountTypeOfProduct {
    private String discountType;
    private Integer countType;
}
