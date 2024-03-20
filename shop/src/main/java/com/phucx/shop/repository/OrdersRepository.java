package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import com.phucx.shop.model.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer>{
       @Procedure("CustOrdersDetail")
       Integer custOrdersDetail(int orderID);
}
