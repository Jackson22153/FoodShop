package com.phucx.order.service.discount;

import java.util.List;

import com.phucx.order.model.DiscountDetail;
import com.phucx.order.model.OrderWithProducts;

public interface DiscountService {
    // public Discount insertDiscount(DiscountWithProduct discount) throws InvalidDiscountException, RuntimeException;
    // public Boolean updateDiscount(DiscountWithProduct discount) throws InvalidDiscountException;
    // public Boolean updateDiscountStatus(Discount discount) throws InvalidDiscountException;
    // // validate discount of a specific product
    // public Boolean validateDiscount(Integer productID, OrderItemDiscount discount) 
    //     throws InvalidDiscountException;
    // public Boolean validateDiscountsOfProduct(OrderItem product) throws InvalidDiscountException;
    // public Page<Discount> getDiscounts(int pageNumber, int pageSize);
    // public Discount getDiscount(String discountID) throws InvalidDiscountException;
    // public DiscountDetail getDiscountDetail(String discountID);

    // public Page<DiscountType> getDiscountTypes(int pageNumber, int pageSize);
    // public Page<DiscountDetail> getDiscountsByProduct(int productID, int pageNumber, int pageSize);

    public DiscountDetail getDiscount(String discountID);
    public List<DiscountDetail> getDiscounts(List<String> discountIDs);

    public Boolean validateDiscount(OrderWithProducts order);
    
} 
