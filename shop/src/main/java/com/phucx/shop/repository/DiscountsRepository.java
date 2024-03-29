package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.Discounts;

@Repository
public interface DiscountsRepository extends JpaRepository<Discounts, Integer>{
    
}
