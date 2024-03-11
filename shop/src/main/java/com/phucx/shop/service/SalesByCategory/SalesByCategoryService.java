package com.phucx.shop.service.SalesByCategory;

import org.springframework.data.domain.Page;

import com.phucx.shop.model.SalesByCategory;

public interface SalesByCategoryService {
    Page<SalesByCategory> findSalesOfProductsPerCategory(int pageNumber, int pageSize);
    
} 
