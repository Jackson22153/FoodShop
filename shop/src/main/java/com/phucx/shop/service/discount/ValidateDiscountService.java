package com.phucx.shop.service.discount;

import java.util.List;

import com.phucx.shop.model.ProductDiscountsDTO;
import com.phucx.shop.model.ResponseFormat;

public interface ValidateDiscountService {
    // validate discount of a specific product
    public ResponseFormat validateDiscountsOfProducts(List<ProductDiscountsDTO> productsDiscounts);

}
