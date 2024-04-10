package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.account.model.ProductsDiscounts;

@Repository
public interface ProductsDiscountsRepository extends JpaRepository<ProductsDiscounts, ProductsDiscounts>{

}
