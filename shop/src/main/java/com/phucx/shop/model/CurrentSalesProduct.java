package com.phucx.shop.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class CurrentSalesProduct {
    private Integer productID;
    private String productName;
    private Integer supplierID;
    private Integer categoryID;
    private String quantityPerUnit;
    private BigDecimal unitPrice;
    private Integer unitsInStock;

    private String discountID;
    private Integer discountPercent;

    private String picture;
    private String categoryName;
    private String companyName;
    private String description;
}
