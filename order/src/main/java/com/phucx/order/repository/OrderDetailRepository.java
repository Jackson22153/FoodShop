package com.phucx.order.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.order.compositeKey.OrderDetailKey;
import com.phucx.order.model.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey>{
    // @Query("""
    //     SELECT od FROM OrderDetail od \
    //     WHERE od.key.order.orderID=?1 AND \
    //         od.key.order.status=?2  
    //     """)
    // List<OrderDetail> findByOrderStatus(String orderID, OrderStatus status);

    @Query("""
        SELECT od FROM OrderDetail od \
        WHERE od.orderID=?1
        """)
    List<OrderDetail> findByOrderID(String orderID);

    @Procedure("InsertOrderDetail")
    Boolean insertOrderDetail(Integer productID, String orderID, BigDecimal unitPrice, Integer quantity);
}
