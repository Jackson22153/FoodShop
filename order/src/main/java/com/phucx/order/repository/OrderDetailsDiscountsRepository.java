package com.phucx.order.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.order.compositeKey.OrderDetailsDiscountsID;
import com.phucx.order.model.OrderDetailsDiscounts;

@Repository
public interface OrderDetailsDiscountsRepository extends JpaRepository<OrderDetailsDiscounts, OrderDetailsDiscountsID> {
    @Modifying
    @Transactional
    @Procedure("InsertOrderDetailDiscount")
    Boolean insertOrderDetailDiscount(Integer orderID, Integer productID, String discountID, LocalDateTime appliedDate);

    // @Query("""
    //     SELECT count(d.discountID) \
    //     from OrderDetailsDiscounts odd join Discount d on odd.id.discount.discountID=d.discountID \
    //     where odd.id.orderDetail.key.order.orderID=?1 and odd.id.orderDetail.key.product.productID=?2 and d.discountType=?3    
    //     """)
    // Integer countTypeDiscount(Integer orderID, Integer productID, String discountType);

    // @Query("""
    //     select count(odd.id.orderDetail.key.product.productID) \
    //     from OrderDetailsDiscounts odd join Discount d on odd.id.discount.discountID=d.discountID \
    //     where odd.id.orderDetail.key.order.orderID=?1 and odd.id.orderDetail.key.product.productID=?2 
    //             and d.discountType.discountTypeID=?3 \
    //     """)
    // Integer findDiscountTypeOfProduct(Integer orderID, Integer productID, Integer discountTypeID);

    @Query("""
        SELECT odd \
        FROM  OrderDetailsDiscounts odd \
        WHERE odd.id.orderDetail.key.order.orderID=?1 AND odd.id.orderDetail.key.product.productID=?2 
        """)
    List<OrderDetailsDiscounts> findByOrderDetail(Integer orderID, Integer productID);
}