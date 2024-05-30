package com.phucx.order.service.product;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.model.Product;

public interface ProductService {
    // update product's instocks
    public Boolean updateProductInStocks(List<Product> products) throws JsonProcessingException;
    // get product
    public List<Product> getProducts(List<Integer> productIDs) throws JsonProcessingException;
    public Product getProduct(int productID) throws JsonProcessingException;
}
