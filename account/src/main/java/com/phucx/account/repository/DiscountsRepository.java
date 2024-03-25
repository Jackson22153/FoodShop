package com.phucx.account.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.Discounts;


@Repository
public interface DiscountsRepository extends JpaRepository<Discounts, Integer>{
    @Modifying
    @Transactional
    @Procedure(name = "insertDiscount")
    public void insertDiscount(Float discountAmount, LocalDateTime startDate, LocalDateTime endDate, Integer productID);

    @Modifying
    @Transactional
    @Procedure(name = "updateDiscount")
    public Integer updateDiscount(Float discountAmount, LocalDateTime startDate, LocalDateTime endDate, Integer productID);
}
