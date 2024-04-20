package com.phucx.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Integer productID;
    private String productName;
    private Double unitPrice;
    private Integer quantity;
    private Integer discount;
    private Double extendedPrice;
    private String picture;
}
