package com.phucx.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.payment.model.PaymentDetails;
import java.util.Optional;


@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, String> {
    
    Optional<PaymentDetails> findByOrderID(String orderID);
}
