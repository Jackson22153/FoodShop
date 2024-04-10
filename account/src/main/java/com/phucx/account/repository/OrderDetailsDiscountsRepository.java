package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.account.compositeKey.OrderDetailsDiscountsID;
import com.phucx.account.model.OrderDetailsDiscounts;

@Repository
public interface OrderDetailsDiscountsRepository extends JpaRepository<OrderDetailsDiscounts, OrderDetailsDiscountsID> {
    
}
