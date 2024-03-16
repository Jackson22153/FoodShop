package com.phucx.shop.service.products;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.shop.model.CurrentProductList;
import com.phucx.shop.model.Products;

public interface ProductsService {
    public CurrentProductList getCurrentProduct(int productID);
    public List<CurrentProductList> getCurrentProductList();
    public Page<CurrentProductList> getCurrentProductList(int pageNumber, int pageSize);

    public Products getProduct(int productID);
    public List<Products> getProducts();
    public Page<Products> getProducts(int pageNumber, int pageSize);
    public List<Products> getProducts(String productName);
    public Page<Products> getProductsByName(int pageNumber, int pageSize, String productName);
    public Page<Products> getProductsByCategoryName(int pageNumber, int pageSize, String categoryName);
    public List<Products> getRecommendedProducts();
    public Page<Products> searchProductByName(String productName, int pageNumber, int pageSize);
}
