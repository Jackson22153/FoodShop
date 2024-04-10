package com.phucx.account.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.constant.DiscountType;
import com.phucx.account.model.Discount;


@Repository
public interface DiscountRepository extends JpaRepository<Discount, String>{
    // @Modifying
    // @Transactional
    // @Procedure(name = "insertDiscount")
    // public void insertDiscount(BigDecimal discountPercent, LocalDateTime startDate, LocalDateTime endDate, Integer productID);

    // @Modifying
    // @Transactional
    // @Procedure(name = "updateDiscount")
    // public Integer updateDiscount(BigDecimal discountPercent, LocalDateTime startDate, LocalDateTime endDate, Integer productID);


    @Modifying
    @Transactional
    @Query("""
        UPDATE Discount SET active=?2 WHERE discountID=?1
            """)
    public Integer updateDiscountStatus(String discountID, boolean status);

    @Query("""
        SELECT d \
        FROM ProductsDiscounts pd JOIN Discount d ON pd.discountID.discountID=d.discountID \
        WHERE d.discountID=?1 AND pd.productID.productID=?2
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
}
