package com.phucx.shop.service.product;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.shop.model.CurrentProductList;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDetail;

public interface ProductService {
    // update product detail
    public boolean updateProductDetail(ProductDetail productDetail);
    // insert product
    public boolean insertProductDetail(ProductDetail productDetail);
    // get product
    public CurrentProductList getCurrentProduct(int productID);
    public List<CurrentProductList> getCurrentProductList();
    public Page<CurrentProductList> getCurrentProductList(int pageNumber, int pageSize);
    public Page<CurrentProductList> getCurrentProductsByCategoryName(String categoryName, int pageNumber, int pageSize);
    public ProductDetail getProductDetail(int productID);

    public Product getProduct(int productID);
    public List<Product> getProducts();
    public Page<Product> getProducts(int pageNumber, int pageSize);
    public List<Product> getProducts(String productName);
    public Page<Product> getProductsByName(int pageNumber, int pageSize, String productName);
    public Page<Product> getProductsByCategoryName(int pageNumber, int pageSize, String categoryName);
    // 
    public List<CurrentProductList> getRecommendedProducts(int pageNumber, int pageSize);
    public Page<CurrentProductList> getRecommendedProductsByCategory(int productID, String categoryName, int pageNumber, int pageSize);
    // search product
    public Page<CurrentProductList> searchCurrentProducts(String productName, int pageNumber, int pageSize);
}
