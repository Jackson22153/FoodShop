package com.phucx.account.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class ProductWithBriefDiscount {
    private Integer productID;
    private String productName;
    private Double unitPrice;
    private Integer quantity;
    private String picture;
    private List<DiscountBreifInfo> discounts;
    private Integer totalDiscount;
    private Double extendedPrice;
    
    public ProductWithBriefDiscount(Integer productID, String productName, Double unitPrice, Integer quantity,
            String picture, List<DiscountBreifInfo> discounts, Integer totalDiscount, Double extendedPrice) {
        this.productID = productID;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.picture = picture;
        this.discounts = discounts;
        this.totalDiscount = totalDiscount;
        this.extendedPrice = extendedPrice;
    }

    public ProductWithBriefDiscount(Integer productID, String productName, Double unitPrice, Integer quantity,
            String picture, Double extendedPrice) {
        this();
        this.productID = productID;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.picture = picture;
        this.extendedPrice = extendedPrice;
    }

    public ProductWithBriefDiscount() {
        this.discounts = new ArrayList<>();
        this.totalDiscount = Integer.valueOf(0);
    }
}
