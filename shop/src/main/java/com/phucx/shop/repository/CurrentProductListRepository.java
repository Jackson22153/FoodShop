package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.CurrentProductList;

@Repository
public interface CurrentProductListRepository extends JpaRepository<CurrentProductList, Integer>{
    
}
