package com.phucx.payment.service.paymentHandler.imp;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.payment.constant.PaymentStatusConstant;
import com.phucx.payment.exception.PaymentNotFoundException;
import com.phucx.payment.model.Payment;
import com.phucx.payment.model.PaymentDTO;
import com.phucx.payment.service.payment.PaymentManagementService;
import com.phucx.payment.service.paymentHandler.CODHandlerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CODHandlerServiceImp implements CODHandlerService{
    @Autowired
    private PaymentManagementService paymentManagementService;

    @Override
    public Payment createPayment(PaymentDTO paymentDTO) {
        log.info("createPayment(paymentDTO={})", paymentDTO);
        // create payment details
        String paymentID = UUID.randomUUID().toString();
        LocalDateTime createdDateTime = LocalDateTime.now();
        String state = PaymentStatusConstant.PENDING.name().toLowerCase();
        String method = paymentDTO.getMethod();

        Payment payment = new Payment(paymentID, createdDateTime, paymentDTO.getAmount(), 
            state, paymentDTO.getCustomerID(), paymentDTO.getOrderID());
        // save payment
        Boolean result = paymentManagementService.savePayment(paymentID, createdDateTime, 
            paymentDTO.getAmount(), state, paymentDTO.getCustomerID(), paymentDTO.getOrderID(), method);
        if(!result){
            log.error("Error while saving payment: {}", paymentDTO);
            return null;
        }
        return payment;
    }

    @Override
    public Boolean paymentSuccessfully(String paymentID) {
        log.info("paymentSuccessfully(paymentID={})", paymentID);
        try {
            String state = PaymentStatusConstant.SUCCESSFUL.name().toLowerCase();
            Boolean result = paymentManagementService.updatePaymentStatus(paymentID, state);
            return result;
        } catch (PaymentNotFoundException e) {
            log.error("Error: {}", e.getMessage());
            return false;
        } 
    }

    @Override
    public Boolean paymentCancelled(String paymentID) {
        log.info("paymentCancelled(paymentID={})", paymentID);
        try {
            String state = PaymentStatusConstant.FAILED.name().toLowerCase();
            Boolean result = paymentManagementService.updatePaymentStatus(paymentID, state);
            return result;
        } catch (PaymentNotFoundException e) {
            log.error("Error: {}", e.getMessage());
            return false;
        } 
    }
    
}
