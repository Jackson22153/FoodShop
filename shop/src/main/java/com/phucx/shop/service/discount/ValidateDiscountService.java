package com.phucx.shop.service.discount;

import java.time.LocalDateTime;
import java.util.List;

import com.phucx.shop.exceptions.InvalidDiscountException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.ProductDiscountsDTO;

public interface ValidateDiscountService {
    // validate discount of a specific product
    public Boolean validateDiscount(Integer productID, String discountID, LocalDateTime appliedDate) throws NotFoundException;
    public Boolean validateDiscountsOfProduct(ProductDiscountsDTO productDiscounts) throws InvalidDiscountException;
    public Boolean validateDiscountsOfProducts(List<ProductDiscountsDTO> productsDiscounts);
}
