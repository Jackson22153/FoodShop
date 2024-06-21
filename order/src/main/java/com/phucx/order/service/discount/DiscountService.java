package com.phucx.order.service.discount;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.DiscountDetail;
import com.phucx.order.model.ProductDiscountsDTO;

public interface DiscountService {
    public DiscountDetail getDiscount(String discountID) throws JsonProcessingException, NotFoundException;
    public List<DiscountDetail> getDiscounts(List<String> discountIDs) throws JsonProcessingException, NotFoundException;
    public Boolean validateDiscount(List<ProductDiscountsDTO> productsDiscounts) throws JsonProcessingException;
    
} 
