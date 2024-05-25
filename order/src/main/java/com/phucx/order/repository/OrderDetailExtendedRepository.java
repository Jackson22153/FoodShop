package com.phucx.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.order.compositeKey.OrderDetailKey;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.model.OrderDetailExtended;

import java.util.List;


@Repository
public interface OrderDetailExtendedRepository extends JpaRepository<OrderDetailExtended, OrderDetailKey>{


    List<OrderDetailExtended> findByOrderID(String orderID);
    List<OrderDetailExtended> findByOrderIDAndStatus(String orderID, OrderStatus status);   

    Page<OrderDetailExtended> findAllByCustomerIDOrderByOrderIDDesc(String customerID, Pageable pageable);
    Page<OrderDetailExtended> findAllByEmployeeIDOrderByOrderIDDesc(String employeeID, Pageable pageable);

    @Query("""
        SELECT ode \
        FROM Order o JOIN OrderDetailExtended ode ON o.orderID=ode.orderID \
        WHERE o.customerID=?1 AND o.status=?2 \
        ORDER BY o.orderID DESC
            """)
    Page<OrderDetailExtended> findAllByCustomerIDAndStatusOrderByDesc(
        String customerID, OrderStatus status, Pageable pageable);

    @Query("""
        SELECT ode \
        FROM Order o JOIN OrderDetailExtended ode ON o.orderID=ode.orderID \
        WHERE o.employeeID=?1 AND o.status=?2 \
        ORDER BY o.orderID DESC
            """)
    Page<OrderDetailExtended> findAllByEmployeeIDAndStatusOrderByDesc(
        String employeeID, OrderStatus status, Pageable pageable);

    @Query("""
        SELECT ode \
        FROM Order o JOIN OrderDetailExtended ode ON o.orderID=ode.orderID \
        WHERE o.status=?1 \
        ORDER BY o.orderID DESC
            """)
    Page<OrderDetailExtended> findAllByStatusOrderByDesc(OrderStatus status, Pageable pageable);
}
