package com.phucx.shop.service.salesByCategory;

import org.springframework.data.domain.Page;

import com.phucx.shop.model.SalesByCategory;

public interface SalesByCategoryService {
    Page<SalesByCategory> findSalesOfProductsPerCategory(int pageNumber, int pageSize);
    
} 
