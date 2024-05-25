package com.phucx.order.service.product;

import java.util.List;

import com.phucx.order.model.Product;

public interface ProductService {
    // public List<Product> getProducts(List<Integer> productIDs);
    
    public Boolean updateProductInStocks(List<Product> products);
    public List<Product> getProducts(List<Integer> productIDs);
    public Product getProduct(int productID);
}
