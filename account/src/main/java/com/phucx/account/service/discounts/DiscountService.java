package com.phucx.account.service.discounts;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;

import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.model.Discount;
import com.phucx.account.model.DiscountWithProduct;
import com.phucx.account.model.OrderItem;

public interface DiscountService {
    public Discount insertDiscount(DiscountWithProduct discount) 
        throws InvalidDiscountException, RuntimeException;
    public Boolean updateDiscount(Discount discount) throws InvalidDiscountException;
    public Boolean updateDiscountStatus(Discount discount) throws InvalidDiscountException;
    // validate discount of a specific product
    public Boolean validateDiscount(OrderItem item);
    public Page<Discount> getDiscounts(int pageNumber, int pageSize);
    public Discount getDiscount(String discountID) 
        throws NoSuchElementException, NullPointerException;
    
} 
