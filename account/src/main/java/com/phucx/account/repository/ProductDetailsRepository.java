package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.ProductDetails;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Integer>{
    // @Query(value = """
    //     exec updateProduct ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11         
    // """, nativeQuery = true)
    @Procedure(name = "updateProduct")
    void updateProduct(
        @Param("productId") Integer productID, 
        @Param("productName") String productName, 
        @Param("quantityPerUnit") String quantityPerUnit, 
        @Param("unitPrice") Double unitPrice, 
        @Param("unitsInStock") Integer unitsInStock, 
        @Param("unitsOnOrder") Integer unitsOnOrder, 
        @Param("reorderLevel") Integer reorderLevel, 
        @Param("discontinued") Boolean discontinued,
        @Param("picture") String picture, 
        @Param("categoryID")Integer categoryID,
        @Param("supplierID") Integer supplierID
    );


}
