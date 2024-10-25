package com.phucx.model;

import java.io.Serializable;

public class ProductStockTableType implements Serializable{
    private Integer productID;
    private Integer unitsInStock;
    public ProductStockTableType() {
    }
    public ProductStockTableType(Integer productID, Integer unitsInStock) {
        this.productID = productID;
        this.unitsInStock = unitsInStock;
    }
    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }
    public Integer getUnitsInStock() {
        return unitsInStock;
    }
    public void setUnitsInStock(Integer unitsInStock) {
        this.unitsInStock = unitsInStock;
    }
}
