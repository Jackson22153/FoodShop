package com.phucx.account.service.discounts;

import org.springframework.data.domain.Page;

import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.model.Discount;
import com.phucx.account.model.DiscountWithProduct;
import com.phucx.account.model.OrderItem;
import com.phucx.account.model.OrderItemDiscount;

public interface DiscountService {
    public Discount insertDiscount(DiscountWithProduct discount) 
        throws InvalidDiscountException, RuntimeException;
    public Boolean updateDiscount(DiscountWithProduct discount) throws InvalidDiscountException;
    public Boolean updateDiscountStatus(Discount discount) throws InvalidDiscountException;
    // validate discount of a specific product
    public Boolean validateDiscount(Integer productID, OrderItemDiscount discount) 
        throws InvalidDiscountException;
    public Boolean validateDiscountsOfProduct(OrderItem product) throws InvalidDiscountException;
    public Page<Discount> getDiscounts(int pageNumber, int pageSize);
    public Discount getDiscount(String discountID) throws InvalidDiscountException;
    
} 
