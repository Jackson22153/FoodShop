package com.phucx.model;

import java.util.List;

public class ProductDTO extends DataDTO{
    private Integer productID;
    private List<Integer> productIds;
    private List<ProductDiscountsDTO> products;
    private List<ProductStockTableType> productStocks;
    public ProductDTO(Integer productID, List<Integer> productIds, List<ProductDiscountsDTO> products,
            List<ProductStockTableType> productStocks) {
        this.productID = productID;
        this.productIds = productIds;
        this.products = products;
        this.productStocks = productStocks;
    }
    public ProductDTO() {
    }
    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }
    public List<Integer> getProductIds() {
        return productIds;
    }
    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }
    public List<ProductDiscountsDTO> getProducts() {
        return products;
    }
    public void setProducts(List<ProductDiscountsDTO> products) {
        this.products = products;
    }
    public List<ProductStockTableType> getProductStocks() {
        return productStocks;
    }
    public void setProductStocks(List<ProductStockTableType> productStocks) {
        this.productStocks = productStocks;
    }

}
