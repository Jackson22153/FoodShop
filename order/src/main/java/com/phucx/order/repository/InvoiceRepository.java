package com.phucx.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.order.compositeKey.OrderDetailDiscountID;
import com.phucx.order.model.Invoice;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, OrderDetailDiscountID> {
    List<Invoice> findByOrderIDAndCustomerID(String orderID, String customerID);
}
