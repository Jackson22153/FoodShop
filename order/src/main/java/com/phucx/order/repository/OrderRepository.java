package com.phucx.order.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.order.constant.OrderStatus;
import com.phucx.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

    @Modifying
    @Transactional
    @Query("""
        UPDATE Order SET status=?2 WHERE orderID=?1    
        """)
    public Integer updateOrderStatus(Integer orderID, OrderStatus status);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Order SET employeeID=?2 WHERE orderID=?1    
        """)
    public Integer updateOrderEmployeeID(Integer orderID, String employeeID);
    
    @Query("""
        SELECT o FROM Order o WHERE o.status=?1    
        """)
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("""
        SELECT o FROM Order o WHERE o.status=?1 AND o.orderID=?2   
        """)
    Optional<Order> findByStatusAndOrderID(OrderStatus status, int orderID);

    @Query("""
        SELECT o FROM Order o WHERE o.status=?1 AND o.employeeID=?2
        """)
    Page<Order> findByStatusAndEmployeeID(OrderStatus status, String employeeID, Pageable pageable);

    @Query("""
        SELECT o FROM Order o WHERE o.customerID=?1
        """)
    Page<Order> findByCustomerID(String customerID, Pageable page);    

    @Query("""
        SELECT o FROM Order o WHERE o.employeeID=?1 AND o.orderID=?2
            """)
    Optional<Order> findByEmployeeIDAndOrderID(String employeeID, int orderID);
} 
