package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String>{
    
}
