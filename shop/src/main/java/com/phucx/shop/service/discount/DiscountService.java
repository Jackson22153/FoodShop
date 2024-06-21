package com.phucx.shop.service.discount;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.shop.exceptions.InvalidDiscountException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.Discount;
import com.phucx.shop.model.DiscountDetail;
import com.phucx.shop.model.DiscountType;
import com.phucx.shop.model.DiscountWithProduct;
import com.phucx.shop.model.ProductDiscountsDTO;

public interface DiscountService {
    // insert discount
    public Discount insertDiscount(DiscountWithProduct discount) throws InvalidDiscountException, NotFoundException;
    // update discount
    public Boolean updateDiscount(DiscountWithProduct discount) throws InvalidDiscountException, NotFoundException;
    public Boolean updateDiscountStatus(Discount discount) throws NotFoundException;
    // validate discount of a specific product
    public Boolean validateDiscount(Integer productID, String discountID, LocalDateTime appliedDate) throws NotFoundException;
    public Boolean validateDiscountsOfProduct(ProductDiscountsDTO productDiscounts) throws InvalidDiscountException;
    public Boolean validateDiscountsOfProducts(List<ProductDiscountsDTO> productsDiscounts);
    // get discount
    public Page<Discount> getDiscounts(int pageNumber, int pageSize);
    public Discount getDiscount(String discountID) throws NotFoundException;
    public DiscountDetail getDiscountDetail(String discountID) throws NotFoundException;
    public List<DiscountDetail> getDiscountDetails(List<String> discountIDs);
    // discount type
    public Page<DiscountType> getDiscountTypes(int pageNumber, int pageSize);
    public Page<DiscountDetail> getDiscountsByProduct(int productID, int pageNumber, int pageSize) throws NotFoundException;
    public DiscountType getDiscountType(int discountTypeID) throws NotFoundException;
} 
