package com.phucx.account.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.constraint.OrderStatus;
import com.phucx.account.model.Orders;



@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer>{

    @Modifying
    @Transactional
    @Query("""
        UPDATE Orders SET status=?2 WHERE orderID=?1    
        """)
    public Integer updateOrderStatus(Integer orderID, OrderStatus status);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Orders SET employeeID.employeeID=?2 WHERE orderID=?1    
        """)
    public Integer updateOrderEmployeeID(Integer orderID, String employeeID);
    
    @Query("""
        SELECT o FROM Orders o WHERE o.status=?1    
        """)
    Page<Orders> findByStatus(OrderStatus status, Pageable pageable);
    
} 
