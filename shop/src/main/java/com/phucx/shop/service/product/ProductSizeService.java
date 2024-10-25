package com.phucx.shop.service.product;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.phucx.shop.exceptions.EntityExistsException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.ProductSize;
import com.phucx.shop.model.ProductSizeInfo;

public interface ProductSizeService {
    // update productsize
    @CacheEvict(cacheNames = {"productsize"}, key = "#productSize.productID")
    public Boolean updateProductSize(ProductSize productSize) throws NotFoundException;
    @CacheEvict(cacheNames = {"productsize"})
    public void updateProductSizeByProductName(List<ProductSizeInfo> productSizeInfos) throws NotFoundException;
    // insert product
    public Boolean createProduct(ProductSizeInfo productSizeInfo) throws EntityExistsException;
    public Boolean createProductSize(ProductSize productSize) throws NotFoundException;
    public Boolean createProductSizes(List<ProductSize> productSizes) throws NotFoundException;
    // get 
    @Cacheable(cacheNames = "productsize", key = "#productID")
    public ProductSize getProductSize(Integer productID) throws NotFoundException;
    
}
