package com.phucx.shop.service.product;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDetail;

public interface ProductService {
    // update product detail
    public boolean updateProductDetail(ProductDetail productDetail);
    // insert product
    public boolean insertProductDetail(ProductDetail productDetail);
    // get product
    public CurrentProduct getCurrentProduct(int productID);
    public List<CurrentProduct> getCurrentProduct();
    public Page<CurrentProduct> getCurrentProduct(int pageNumber, int pageSize);
    public Page<CurrentProduct> getCurrentProductsByCategoryName(String categoryName, int pageNumber, int pageSize);
    public ProductDetail getProductDetail(int productID);

    public Product getProduct(int productID);
    public Product getProduct(Integer productID, Boolean discontinued);
    public List<Product> getProducts();
    public List<Product> getProducts(List<Integer> productIDs);
    public Page<Product> getProducts(int pageNumber, int pageSize);
    public List<Product> getProducts(String productName);
    public Page<Product> getProductsByName(int pageNumber, int pageSize, String productName);
    public Page<Product> getProductsByCategoryName(int pageNumber, int pageSize, String categoryName);
    // 
    public List<CurrentProduct> getCurrentProducts(List<Integer> productIDs);
    public List<CurrentProduct> getRecommendedProducts(int pageNumber, int pageSize);
    public Page<CurrentProduct> getRecommendedProductsByCategory(int productID, String categoryName, int pageNumber, int pageSize);
    // search product
    public Page<CurrentProduct> searchCurrentProducts(String productName, int pageNumber, int pageSize);
}
