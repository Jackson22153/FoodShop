package com.phucx.payment.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.payment.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>{
    
    @Procedure("SavePayment")
    public void savePayment(String paymentID, LocalDateTime paymentDate, 
        Double amount, String customerID, String orderID, String status, String paymentMethod);

    @Procedure("SaveFullPayment")
    public void saveFullPayment(String paymentID, LocalDateTime paymentDate, 
        Double amount, String transactionID, String customerID, String orderID, 
        String status, String paymentMethod);

    @Procedure("UpdatePayment")
    public void updatePayment(String paymentID, String transactionID, String status);

    @Procedure("UpdatePaymentStatus")
    public void updatePaymentStatus(String paymentID, String status);

    @Procedure("UpdatePaymentStatusByOrderID")
    public void UpdatePaymentStatusByOrderID(String orderID, String status);

    @Query("""
        SELECT p FROM Payment p \
        WHERE p.orderID=?1
            """)
    public Optional<Payment> findByOrderID(String orderID);
}
