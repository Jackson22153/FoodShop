package com.phucx.account.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.account.model.DiscountDetail;

import jakarta.transaction.Transactional;

@Repository
public interface DiscountDetailRepository extends JpaRepository<DiscountDetail, String>{
    @Query("""
        SELECT d \
        FROM DiscountDetail d JOIN ProductDiscount pd ON d.discountID=pd.discount.discountID \
        WHERE pd.product.productID=?1
            """)
    Page<DiscountDetail> findByProductID(int productID, Pageable pageable);

    @Modifying
    @Transactional
    @Procedure("UpdateDiscount")
    Boolean updateDiscount(String discountID, Integer discountPercent, String discountCode, 
        LocalDateTime startDate, LocalDateTime endDate, Boolean active, String discountType);

    @Modifying
    @Transactional
    @Procedure("InsertDiscount")
    Boolean insertDiscount(String discountID, Integer discountPercent, String discountCode, 
        LocalDateTime startDate, LocalDateTime endDate, Boolean active, String discountType, Integer productID);
    
    @Modifying
    @Transactional
    @Procedure("UpdateDiscountStatus")
    Boolean updateDiscountStatus(String discountID, Boolean active);
}
