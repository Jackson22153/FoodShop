package com.phucx.shop.service.SalesByCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.SalesByCategory;
import com.phucx.shop.repository.SalesByCategoryRepository;

@Service
public class SalesByCategoryServiceImp implements SalesByCategoryService{
    @Autowired
    private SalesByCategoryRepository salesByCategoryRepository;
    @Override
    public Page<SalesByCategory> findSalesOfProductsPerCategory(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);   
        return salesByCategoryRepository.findAll(page);
    }
    
}
