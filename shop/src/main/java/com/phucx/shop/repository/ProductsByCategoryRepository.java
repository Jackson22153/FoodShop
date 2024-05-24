package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.compositeKey.ProductsByCategoryID;
import com.phucx.shop.model.ProductsByCategory;

@Repository
public interface ProductsByCategoryRepository extends JpaRepository<ProductsByCategory, ProductsByCategoryID>{
    
}
