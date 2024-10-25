package com.phucx.payment.service.product;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.payment.model.Product;
import com.phucx.payment.model.ShippingProduct;

public interface ProductService {
    // get product
    public List<Product> getProducts(List<Integer> productIDs) throws JsonProcessingException;
    public Product getProduct(int productID) throws JsonProcessingException;
    public ShippingProduct getShippingProduct(String encodedCartJson) throws JsonProcessingException;
}
