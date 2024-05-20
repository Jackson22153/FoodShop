package com.phucx.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.order.model.DiscountType;
import java.util.Optional;


@Repository
public interface DiscountTypeRepository extends JpaRepository<DiscountType, Integer>{
    Optional<DiscountType> findByDiscountType(String discountType);
}
