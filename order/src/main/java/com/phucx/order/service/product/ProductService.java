package com.phucx.order.service.product;

import java.util.List;

import com.phucx.order.model.Product;

public interface ProductService {
    public Product getProduct(int productID);
    public Boolean updateProductInStocks(int productID, int value);
    public List<Product> getProducts(List<Integer> productIDs);
}
