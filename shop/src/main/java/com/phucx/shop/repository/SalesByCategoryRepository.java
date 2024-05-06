package com.phucx.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.SalesByCategory;

@Repository
public interface SalesByCategoryRepository extends JpaRepository<SalesByCategory, Integer>{
    @Query("""
        SELECT s FROM SalesByCategory s JOIN Product p ON s.productID=p.productID \
        WHERE p.discontinued=false \
        ORDER BY productSales DESC
            """)
    public Page<SalesByCategory> findAllContinuedProductsOrderByProductSalesDesc(Pageable page);
    
}
