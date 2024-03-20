package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.account.model.Products;


@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {
}
