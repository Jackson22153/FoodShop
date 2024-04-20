package com.phucx.account.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.account.compositeKey.OrderDetailsExtendedID;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.model.OrderDetailsExtendedStatus;

@Repository
public interface OrderDetailsExtendedStatusRepository extends JpaRepository<OrderDetailsExtendedStatus, OrderDetailsExtendedID>{
    @Query("""
        SELECT ode \
        FROM Orders o JOIN OrderDetailsExtendedStatus ode ON o.orderID=ode.orderID \
        WHERE o.customer.customerID=?1 \
        ORDER BY o.orderID DESC
            """)
    Page<OrderDetailsExtendedStatus> findAllByCustomerIDOrderByDesc(String customerID, Pageable pageable);
    @Query("""
        SELECT ode \
        FROM Orders o JOIN OrderDetailsExtendedStatus ode ON o.orderID=ode.orderID \
        WHERE o.customer.customerID=?1 AND o.status=?2 \
        ORDER BY o.orderID DESC
            """)
    Page<OrderDetailsExtendedStatus> findAllByCustomerIDAndStatusOrderByDesc(
        String customerID, OrderStatus status, Pageable pageable);
}
