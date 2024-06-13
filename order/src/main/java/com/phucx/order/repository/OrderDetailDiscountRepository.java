package com.phucx.order.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.order.compositeKey.OrderDetailDiscountID;
import com.phucx.order.model.OrderDetailDiscount;

@Repository
public interface OrderDetailDiscountRepository extends JpaRepository<OrderDetailDiscount, OrderDetailDiscountID> {
    @Modifying
    @Transactional
    @Procedure("InsertOrderDetailDiscount")
    Boolean insertOrderDetailDiscount(String orderID, Integer productID, String discountID, Integer discountPercent, LocalDateTime appliedDate);

    @Query("""
        SELECT odd \
        FROM  OrderDetailDiscount odd \
        WHERE odd.orderID=?1 AND odd.productID=?2 
        """)
    List<OrderDetailDiscount> findByOrderIDAndProductID(String orderID, Integer productID);
}
