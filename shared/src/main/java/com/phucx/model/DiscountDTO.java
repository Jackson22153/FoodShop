package com.phucx.model;

import java.util.List;

public class DiscountDTO extends DataDTO{
    private String discountID;
    private List<String> discountIDs;
    private List<ProductDiscountsDTO> productsDiscounts;
    public DiscountDTO() {
    }
    public DiscountDTO(String discountID, List<String> discountIDs, List<ProductDiscountsDTO> productsDiscounts) {
        this.discountID = discountID;
        this.discountIDs = discountIDs;
        this.productsDiscounts = productsDiscounts;
    }
    public String getDiscountID() {
        return discountID;
    }
    public void setDiscountID(String discountID) {
        this.discountID = discountID;
    }
    public List<String> getDiscountIDs() {
        return discountIDs;
    }
    public void setDiscountIDs(List<String> discountIDs) {
        this.discountIDs = discountIDs;
    }
    public List<ProductDiscountsDTO> getProductsDiscounts() {
        return productsDiscounts;
    }
    public void setProductsDiscounts(List<ProductDiscountsDTO> productsDiscounts) {
        this.productsDiscounts = productsDiscounts;
    }
}
