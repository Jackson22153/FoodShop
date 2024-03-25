package com.phucx.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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

    @Procedure(name = "insertProduct")
    void insertProduct(
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
