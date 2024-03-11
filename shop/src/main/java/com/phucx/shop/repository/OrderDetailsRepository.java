package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.compositeKey.OrderDetailsKey;
import com.phucx.shop.model.OrderDetails;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, OrderDetailsKey>{
      // @Query("""
      //       SELECT od \
      //       FROM OrderDetails od JOIN Orders o ON od.id.orderID=o.orderID \
      //       WHERE o.customerID.customerID= ?1 AND \
      //             o.shippedDate IS NOT NULL AND \
      //             o.status=1 AND \
      //             od.id.orderID IN ?2 \
      //       ORDER BY o.orderID DESC\
      //       """)
      // List<OrderDetails> findDeliveredByCustomerID(@Size(min = 36, max = 36)String customerID, List<Integer> listOrderID);

      // @Query("""
      //       SELECT od \
      //       FROM OrderDetails od JOIN Orders o ON od.id.orderID=o.orderID \
      //       WHERE o.customerID.customerID= ?1 AND \
      //             o.shippedDate IS NULL AND \
      //             o.status=1 AND \
      //             od.id.orderID IN ?2 \
      //       ORDER BY o.orderID DESC\
      //       """)
      // List<OrderDetails> findDeliveringByCustomerID(@Size(min = 36, max = 36)String customerID, List<Integer> listOrderID);

      // @Query("""
      //      SELECT od \
      //      FROM OrderDetails od JOIN Orders o ON od.id.orderID=o.orderID \
      //      WHERE o.customerID.customerID= ?1 AND \
      //            o.status=0 AND \
      //            od.id.orderID IN ?2 \
      //      ORDER BY o.orderID DESC\
      //      """)
      // List<OrderDetails> findAllPendingByCustomerID(@Size(min = 36, max = 36)String customerID, List<Integer> listOrderID);

      // @Query("""
      //       SELECT od \
      //       FROM OrderDetails od \
      //       WHERE od.id.orderID = ?1 AND \
      //             od.orders.customerID.customerID= ?2\
      //       """)
      // OrderDetails findByOrderIDAndCustomerID(Integer orderID, @Size(min = 36, max = 36) String customerID);

      // @Query("""
      //       SELECT od \
      //       FROM OrderDetails od \
      //       WHERE od.id.orderID IN ?1 AND \
      //             od.orders.customerID.customerID= ?2 \
      //       ORDER BY od.id.orderID DESC\
      //       """)
      // List<OrderDetails> findAllByListOrderIDAndCustomerID(List<Integer> orderID, @Size(min = 36, max = 36) String customerID);

      // @Query("""
      //       SELECT od \
      //       FROM OrderDetails od \
      //       WHERE od.id.orderID = ?1 AND \
      //             od.orders.EmployeeID.EmployeeID= ?2\
      //       """)
      // OrderDetails findByOrderIDAndEmployeeID(Integer orderID, @Size(min = 36, max = 36) String employeeID);

      // @Query("""
      //       SELECT od \
      //       FROM OrderDetails od \
      //       WHERE od.id.orderID IN ?1 AND \
      //             od.orders.employeeID.employeeID= ?2 \
      //       ORDER BY od.orders.OrderID DESC\
      //       """)
      // List<OrderDetails> findAllByListOrderIDAndEmployeeID(List<Integer> orderID, @Size(min = 36, max = 36) String employeeID);
}
