package com.phucx.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.order.compositeKey.OrderDetailDiscountID;
import com.phucx.order.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, OrderDetailDiscountID> {
    
}
