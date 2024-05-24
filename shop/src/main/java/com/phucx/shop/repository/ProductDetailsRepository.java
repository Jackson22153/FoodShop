package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.shop.model.ProductDetail;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetail, Integer>{
}
