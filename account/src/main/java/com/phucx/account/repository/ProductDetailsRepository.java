package com.phucx.account.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.phucx.account.model.ProductDetails;

import jakarta.transaction.Transactional;
import java.util.Optional;


@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Integer>{
    @Modifying
    @Transactional
    @Procedure(name = "UpdateProduct")
    Boolean updateProduct(
        @Param("productId") Integer productID, 
        @Param("productName") String productName, 
        @Param("quantityPerUnit") String quantityPerUnit, 
        @Param("unitPrice") BigDecimal unitPrice, 
        @Param("unitsInStock") Integer unitsInStock, 
        @Param("unitsOnOrder") Integer unitsOnOrder, 
        @Param("reorderLevel") Integer reorderLevel, 
        @Param("discontinued") Boolean discontinued,
        @Param("picture") String picture, 
        @Param("description") String description, 
        @Param("categoryID")Integer categoryID,
        @Param("supplierID") Integer supplierID
    );

    @Modifying
    @Transactional
    @Procedure(name = "InsertProduct")
    Boolean insertProduct(
        @Param("productName") String productName, 
        @Param("quantityPerUnit") String quantityPerUnit, 
        @Param("unitPrice") BigDecimal unitPrice, 
        @Param("unitsInStock") Integer unitsInStock, 
        @Param("unitsOnOrder") Integer unitsOnOrder, 
        @Param("reorderLevel") Integer reorderLevel, 
        @Param("discontinued") Boolean discontinued,
        @Param("picture") String picture, 
        @Param("description") String description, 
        @Param("categoryID")Integer categoryID,
        @Param("supplierID") Integer supplierID
    );

    Optional<ProductDetails> findByProductName(String productName);
}
