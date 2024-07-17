package com.phucx.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.DiscountDetail;

import jakarta.transaction.Transactional;

@Repository
public interface DiscountDetailRepository extends JpaRepository<DiscountDetail, String>{
    @Query("""
        SELECT d \
        FROM DiscountDetail d JOIN ProductDiscount pd ON d.discountID=pd.discountID \
        WHERE pd.productID=?1
            """)
    Page<DiscountDetail> findByProductID(int productID, Pageable pageable);

    @Query("""
        SELECT d \
        FROM DiscountDetail d JOIN ProductDiscount pd ON d.discountID=pd.discountID \
        WHERE pd.productID=?2 AND d.discountID IN ?1
        """)
    List<DiscountDetail> findAllByDiscountIDAndProductID(List<String> discountIDs, Integer productID);

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
