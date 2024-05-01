package com.phucx.shop.service.products;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.shop.model.CurrentProductList;
import com.phucx.shop.model.CurrentSalesProduct;
import com.phucx.shop.model.Product;

public interface ProductService {
    public CurrentProductList getCurrentProduct(int productID);
    public List<CurrentProductList> getCurrentProductList();
    public Page<CurrentProductList> getCurrentProductList(int pageNumber, int pageSize);
    public Page<CurrentProductList> searchCurrentProducts(String productName, int pageNumber, int pageSize);
    public Page<CurrentProductList> getCurrentProductsByCategoryName(String categoryName, int pageNumber, int pageSize);
    public CurrentSalesProduct getCurrentSalesProductsByID(int productID);

    public Product getProduct(int productID);
    public List<Product> getProducts();
    public Page<Product> getProducts(int pageNumber, int pageSize);
    public List<Product> getProducts(String productName);
    public Page<Product> getProductsByName(int pageNumber, int pageSize, String productName);
    public Page<Product> getProductsByCategoryName(int pageNumber, int pageSize, String categoryName);
    
    public List<CurrentProductList> getRecommendedProducts(int pageNumber, int pageSize);
    public Page<CurrentProductList> getRecommendedProductsByCategory(int productID, String categoryName, int pageNumber, int pageSize);
}
