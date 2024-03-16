package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.AlphabeticalListOfProducts;

@Repository
public interface AlphabeticalListOfProductsRepository extends JpaRepository<AlphabeticalListOfProducts, Integer>{
    
}
