package com.phucx.shop.service.discount;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;

import com.phucx.shop.exceptions.InvalidDiscountException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.Discount;
import com.phucx.shop.model.DiscountDetail;
import com.phucx.shop.model.DiscountType;
import com.phucx.shop.model.DiscountWithProduct;

public interface DiscountService {
    // insert discount
    public Discount insertDiscount(DiscountWithProduct discount) throws InvalidDiscountException, NotFoundException;
    // update discount
    @Caching(
        evict = {
            @CacheEvict(cacheNames = {"discount", "discountdetail"}, key = "#discount.discountID"),
            @CacheEvict(cacheNames = {"currentproduct", "productdetail", "product", "existedproduct"}, key = "#discount.productID")
        }
    )
    public Boolean updateDiscount(DiscountWithProduct discount) throws InvalidDiscountException, NotFoundException;

    @Caching(
        evict = {
            @CacheEvict(cacheNames = {"discount", "discountdetail"}, key = "#discount.discountID"),
            @CacheEvict(cacheNames = {"currentproduct", "productdetail", "product", "existedproduct"}, key = "#discount.productID")
        }
    )
    public Boolean updateDiscountStatus(DiscountDetail discount) throws NotFoundException;

    // get discount
    @Cacheable(value = "discount", key = "#pageNumber")
    public Page<Discount> getDiscounts(int pageNumber, int pageSize);
    @Cacheable(value = "discount", key = "#discountID")
    public Discount getDiscount(String discountID) throws NotFoundException;
    @Cacheable(value = "discountdetail", key = "#discountID")
    public DiscountDetail getDiscountDetail(String discountID) throws NotFoundException;
    @Cacheable(value = "discountDetail", key = "#discountIDs")
    public List<DiscountDetail> getDiscountDetails(List<String> discountIDs);
    // discount type
    @Cacheable(value = "discountType", key = "#pageNumber")
    public Page<DiscountType> getDiscountTypes(int pageNumber, int pageSize);
    @Cacheable(value = "discountDetail", key = "#productID + ':' + #pageNumber")
    public Page<DiscountDetail> getDiscountsByProduct(int productID, int pageNumber, int pageSize) throws NotFoundException;
    @Cacheable(value = "discountType", key = "#discountTypeID")
    public DiscountType getDiscountType(int discountTypeID) throws NotFoundException;
} 
