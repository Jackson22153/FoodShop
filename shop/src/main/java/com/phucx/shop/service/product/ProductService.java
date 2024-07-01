package com.phucx.shop.service.product;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;

import com.phucx.shop.exceptions.EntityExistsException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.ExistedProduct;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDetail;
import com.phucx.shop.model.ProductDiscountsDTO;
import com.phucx.shop.model.ProductStockTableType;
import com.phucx.shop.model.ResponseFormat;

public interface ProductService {

    // validate products
    public ResponseFormat validateProducts(List<ProductDiscountsDTO> products);
    // update product quantity
    public Boolean updateProductInStock(List<ProductStockTableType> products) throws NotFoundException;
    // update product detail
    @Caching(
        put = {
            @CachePut(cacheNames = {"productdetail"}, key = "#productDetail.productID")
        },
        evict = {
            @CacheEvict(cacheNames = {"currentproduct", "product", "existedproduct"}, key = "#productDetail.productID")
        }
    )
    public ProductDetail updateProductDetail(ProductDetail productDetail) throws NotFoundException;
    // insert product
    public Boolean insertProductDetail(ProductDetail productDetail) throws EntityExistsException;
    // get product
    // current product
    @Cacheable(value = "currentproduct", key = "#productID")
    public CurrentProduct getCurrentProduct(int productID) throws NotFoundException;
    @Cacheable(value = "currentproduct")
    public List<CurrentProduct> getCurrentProduct();
    @Cacheable(value = "currentproduct", key = "#pageNumber")
    public Page<CurrentProduct> getCurrentProduct(int pageNumber, int pageSize);
    @Cacheable(value = "currentproduct", key = "#categoryName + ':' + #pageNumber")
    public Page<CurrentProduct> getCurrentProductsByCategoryName(String categoryName, int pageNumber, int pageSize) throws NotFoundException;
    @Cacheable(value = "currentproduct", key = "#productIDs")
    public List<CurrentProduct> getCurrentProducts(List<Integer> productIDs);

    public List<CurrentProduct> getRecommendedProducts(int pageNumber, int pageSize);
    @Cacheable(value = "currentproduct", key = "#productID + ':' + #categoryName + ':' + #pageNumber")
    public Page<CurrentProduct> getRecommendedProductsByCategory(int productID, String categoryName, int pageNumber, int pageSize);
    // search product
    @Cacheable(value = "currentproduct", key = "#productName + ':' + #pageNumber")
    public Page<CurrentProduct> searchCurrentProducts(String productName, int pageNumber, int pageSize);
    // productdetail
    @Cacheable(value = "productdetail", key = "#productID")
    public ProductDetail getProductDetail(int productID) throws NotFoundException;
    // product
    @Cacheable(value = "product", key = "#productID")
    public Product getProduct(int productID) throws NotFoundException;
    @Cacheable(value = "product", key = "#productID + ':' + #discontinued")
    public Product getProduct(Integer productID, Boolean discontinued) throws NotFoundException;
    @Cacheable(value = "product")
    public List<Product> getProducts();
    @Cacheable(value = "product", key = "#productIDs")
    public List<Product> getProducts(List<Integer> productIDs);
    @Cacheable(value = "product", key = "#pageNumber")
    public Page<Product> getProducts(int pageNumber, int pageSize);
    @Cacheable(value = "product", key = "#productName")
    public List<Product> getProducts(String productName);
    @Cacheable(value = "product", key = "#productName + ':' + #pageNumber")
    public Page<Product> getProductsByName(int pageNumber, int pageSize, String productName);
    @Cacheable(value = "product", key = "#categoryName + ':' + #pageNumber")
    public Page<Product> getProductsByCategoryName(int pageNumber, int pageSize, String categoryName);
    // 
    @Cacheable(value = "existedproduct", key = "#pageNumber")
    public Page<ExistedProduct> getExistedProducts(int pageNumber, int pageSize);

}
