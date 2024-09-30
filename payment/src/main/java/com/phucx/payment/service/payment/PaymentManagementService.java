package com.phucx.payment.service.payment;

import java.time.LocalDateTime;

import com.phucx.payment.constant.PaymentStatusConstant;
import com.phucx.payment.exception.PaymentNotFoundException;

public interface PaymentManagementService {
    public Boolean savePayment(String paymentID, LocalDateTime paymentDate, Double amount, 
        String status, String customerID, String orderID, String method);
    public Boolean savePayment(String paymentID, LocalDateTime paymentDate, String transactionID, 
        Double amount, String status, String customerID, String orderID, String method);
    public Boolean updatePayment(String paymentID, String transactionID, String status) throws PaymentNotFoundException;
    public Boolean updatePaymentStatus(String paymentID, String status) throws PaymentNotFoundException;
    public Boolean updatePaymentStatusByOrderID(String orderID, PaymentStatusConstant status) throws PaymentNotFoundException;

    
    public Boolean updatePaymentAsSuccessfulByOrderIDPerMethod(String orderID) throws PaymentNotFoundException;
    public Boolean updatePaymentAsCanceledByOrderIDPerMethod(String orderID) throws PaymentNotFoundException;
}
