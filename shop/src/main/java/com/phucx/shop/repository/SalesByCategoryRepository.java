package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.SalesByCategory;

@Repository
public interface SalesByCategoryRepository extends JpaRepository<SalesByCategory, SalesByCategory>{

    
}
