package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.ProductDetails;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Integer>{
    
}
