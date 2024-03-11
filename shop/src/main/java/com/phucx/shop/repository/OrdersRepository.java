package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.shop.model.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer>{
       // @Query("""
       //        SELECT o.orderID \
       //        FROM Orders o \
       //        WHERE o.customerID.customerID=?1 AND \
       //              o.status=0 \
       //        ORDER BY o.orderID DESC\
       //        """)
       // Page<Integer> findAllPendingOrders(@Size(min = 36, max = 36)String customerID, Pageable page);

       // @Query("""
       //        SELECT o.orderID \
       //        FROM Orders o \
       //        WHERE o.customerID.customerID=?1 AND \
       //              o.shippedDate IS NOT NULL AND \
       //              o.status =1 \
       //        ORDER BY o.orderID DESC\
       //        """)
       // Page<Integer> findAllDeliveredOrderID(@Size(min = 36, max = 36)String customerID, Pageable page);

       // @Query("""
       //        SELECT o.orderID \
       //        FROM Orders o \
       //        WHERE o.customerID.customerID=?1 AND \
       //              o.shippedDate IS NULL AND \
       //              o.status =1 \
       //        ORDER BY o.orderID DESC\
       //        """)
       // Page<Integer> findAllDeliveringOrderID(@Size(min = 36, max = 36)String customerID, Pageable page);

       // @Query("""
       //        SELECT o.orderID \
       //        FROM Orders o \
       //        WHERE o.employeeID.employeeID=?1 \
       //        ORDER BY o.orderID DESC\
       //        """)
       // Page<Integer> findOrderID(@Size(min = 36, max = 36)String employeeID, Pageable page);

       // @Query("""
       //        SELECT o.orderID \
       //        FROM Orders o \
       //        WHERE o.employeeID.employeeID=?1 AND \
       //              o.status=0 \
       //        ORDER BY o.orderID DESC\
       //        """)
       // Page<Integer> findAllPendingOrderID(@Size(min = 36, max = 36)String employeeID, Pageable page);

       // @Query("""
       //        SELECT o.orderID \
       //        FROM Orders o \
       //        WHERE o.employeeID.employeeID=?1 AND \
       //              o.status=1 \
       //        ORDER BY o.orderID DESC\
       //        """)
       // Page<Integer> findAllSuccessfulOrderID(@Size(min = 36, max = 36)String employeeID, Pageable page);

       // @Query("""
       //        SELECT o \
       //        FROM Orders o \
       //        WHERE o.orderID=?1 AND \
       //              o.customerID.customerID=?2\
       //        """)
       // Orders findByOrderIdAndCustomerId(Integer orderID, @Size(min = 36, max = 36) String customerID);

       // @Transactional
       // @Modifying
       // @Query("""
       //        UPDATE Orders \
       //        SET Status= ?3 \
       //        WHERE OrderID=?1 AND \
       //              customerID.customerID=?2\
       //        """)
       // Integer updateOrderStatusByOrderIdAndCustomerId(Integer orderID, @Size(min = 36, max = 36) String customerID, Boolean status);
}
