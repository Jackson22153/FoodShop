package com.phucx.model;

import java.io.Serializable;
import java.util.List;


public class ProductDiscountsDTO implements Serializable{
    private Integer productID;

    private Integer quantity;

    private List<String> discountIDs;
    private String appliedDate;
    public ProductDiscountsDTO() {
    }
    public ProductDiscountsDTO(Integer productID, Integer quantity, List<String> discountIDs,
            String appliedDate) {
        this.productID = productID;
        this.quantity = quantity;
        this.discountIDs = discountIDs;
        this.appliedDate = appliedDate;
    }
    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public List<String> getDiscountIDs() {
        return discountIDs;
    }
    public void setDiscountIDs(List<String> discountIDs) {
        this.discountIDs = discountIDs;
    }
    public String getAppliedDate() {
        return appliedDate;
    }
    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }
}
