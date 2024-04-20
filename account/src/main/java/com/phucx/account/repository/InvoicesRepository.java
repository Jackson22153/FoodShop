package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.account.compositeKey.OrderDetailsExtendedID;
import com.phucx.account.model.Invoice;
import java.util.List;


@Repository
public interface InvoicesRepository extends JpaRepository<Invoice, OrderDetailsExtendedID>{
    List<Invoice> findByOrderIDAndCustomerIDOrderByProductIDAsc(Integer orderID, String customerID);
}
