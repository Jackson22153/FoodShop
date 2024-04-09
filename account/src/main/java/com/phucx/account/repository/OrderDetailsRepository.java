package com.phucx.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.account.compositeKey.OrderDetailsKey;
import com.phucx.account.constraint.OrderStatus;
import com.phucx.account.model.OrderDetails;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, OrderDetailsKey>{
    @Query("""
        SELECT od FROM OrderDetails od \
        WHERE od.key.orderID.orderID=?1 AND \
            od.key.orderID.status=?2  
        """)
    List<OrderDetails> findByOrderStatus(Integer orderID, OrderStatus status);

    @Query("""
        SELECT od FROM OrderDetails od \
        WHERE od.key.orderID.orderID=?1
        """)
    List<OrderDetails> findByOrderID(Integer orderID);
}
