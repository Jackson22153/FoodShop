package com.phucx.order.service.product;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.model.ProductDiscountsDTO;
import com.phucx.model.ProductStockTableType;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.Product;
import com.phucx.order.model.ResponseFormat;

public interface ProductService {
    // update product's instocks
    public Boolean updateProductsInStocks(List<ProductStockTableType> productStocks) throws JsonProcessingException;
    // get product
    public List<Product> getProducts(List<Integer> productIDs) throws JsonProcessingException;
    public Product getProduct(int productID) throws JsonProcessingException, NotFoundException;
    // validate products, discounts
    public ResponseFormat validateProducts(List<ProductDiscountsDTO> products) throws JsonProcessingException;
    // validate products, discounts and update products instock
    public ResponseFormat validateAndProcessProducts(List<ProductDiscountsDTO> products) throws JsonProcessingException;
}
