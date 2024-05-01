package com.phucx.account.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.Discount;
import com.phucx.account.model.DiscountType;


@Repository
public interface DiscountRepository extends JpaRepository<Discount, String>{
    @Modifying
    @Transactional
    @Query("""
        UPDATE Discount SET active=?2 WHERE discountID=?1
            """)
    public Integer updateDiscountStatus(String discountID, boolean status);

    @Query("""
        SELECT d \
        FROM ProductDiscount pd JOIN Discount d ON pd.discount.discountID=d.discountID \
        WHERE d.discountID=?1 AND pd.product.productID=?2
        """)
    Optional<Discount> findByDiscountIDAndProductID(String discountID, Integer productID);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Discount SET discountPercent=?2, discountType=?3, discountCode=?4, startDate=?5, endDate=?6 \
        WHERE discountID=?1 
            """)
    Integer updateDiscount(String discountID, BigDecimal discountPercent, DiscountType discountType,
        String discountCode, LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
        SELECT d \
        FROM Discount d JOIN ProductDiscount pd ON d.discountID=pd.discount.discountID \
        WHERE pd.product.productID=?1
            """)
    Page<Discount> findByProductID(int productID, Pageable pageable);
}
