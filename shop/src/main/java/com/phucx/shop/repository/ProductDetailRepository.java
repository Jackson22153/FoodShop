package com.phucx.shop.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.phucx.shop.model.ProductDetail;

import jakarta.transaction.Transactional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer>{
    @Modifying
    @Transactional
    @Procedure(name = "UpdateProduct")
    Boolean updateProduct(
        @Param("productId") Integer productID, 
        @Param("productName") String productName, 
        @Param("quantityPerUnit") String quantityPerUnit, 
        @Param("unitPrice") BigDecimal unitPrice, 
        @Param("unitsInStock") Integer unitsInStock, 
        @Param("discontinued") Boolean discontinued,
        @Param("picture") String picture, 
        @Param("description") String description, 
        @Param("categoryID")Integer categoryID
    );

    @Modifying
    @Transactional
    @Procedure(name = "InsertProduct")
    Boolean insertProduct(
        @Param("productName") String productName, 
        @Param("quantityPerUnit") String quantityPerUnit, 
        @Param("unitPrice") BigDecimal unitPrice, 
        @Param("unitsInStock") Integer unitsInStock, 
        @Param("discontinued") Boolean discontinued,
        @Param("picture") String picture, 
        @Param("description") String description, 
        @Param("categoryID")Integer categoryID
    );

    Optional<ProductDetail> findByProductName(String productName);
}
