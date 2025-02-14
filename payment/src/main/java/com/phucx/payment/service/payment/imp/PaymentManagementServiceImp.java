package com.phucx.payment.service.payment.imp;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.payment.constant.PaymentMethodConstant;
import com.phucx.payment.constant.PaymentStatusConstant;
import com.phucx.payment.exception.NotFoundException;
import com.phucx.payment.exception.PaymentNotFoundException;
import com.phucx.payment.model.Payment;
import com.phucx.payment.model.PaymentDetails;
import com.phucx.payment.model.PaymentMethod;
import com.phucx.payment.repository.PaymentDetailsRepository;
import com.phucx.payment.repository.PaymentRepository;
import com.phucx.payment.service.payment.PaymentManagementService;
import com.phucx.payment.service.paymentMethod.PaymentMethodService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentManagementServiceImp implements PaymentManagementService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public Boolean updatePayment(String paymentID, String transactionID, String status) throws PaymentNotFoundException {
        log.info("updatePayment(paymentID={},transactionID={}, status={})", paymentID, transactionID, status);
        if(paymentID==null && status==null) return false;
        Payment payment = paymentRepository.findById(paymentID)
            .orElseThrow(()-> new PaymentNotFoundException("Payment " + paymentID + " does not found"));
        paymentRepository.updatePayment(payment.getPaymentID(), transactionID, status);
        return true;
    }

    @Override
    public Boolean updatePaymentStatus(String paymentID, String status) throws PaymentNotFoundException {
        log.info("updatePaymentStatus(paymentID={}, status={})", paymentID, status);
        if(paymentID==null && status==null) return false;
        Payment payment = paymentRepository.findById(paymentID)
            .orElseThrow(()-> new PaymentNotFoundException("Payment " + paymentID + " does not found"));
        paymentRepository.updatePaymentStatus(payment.getPaymentID(), status);
        return true;
    }

    @Override
    public Boolean savePayment(String paymentID, LocalDateTime paymentDate, Double amount, String status,
            String customerID, String orderID, String method) {
        log.info("savePayment(paumentID={}, paymentDate={}, amount={}, status={}, customerID={}, orderID={}, method={})", 
            paymentID, paymentDate, amount, status, customerID, orderID, method);
        if(paymentID==null) return false;
        String paymentMethod = method.toLowerCase();
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentID);
        if(optionalPayment.isPresent()) return false;
        paymentRepository.savePayment(paymentID, paymentDate, amount, 
            customerID, orderID, status, paymentMethod);
        return true;
    }

    @Override
    public Boolean savePayment(String paymentID, LocalDateTime paymentDate, String transactionID, Double amount,
            String status, String customerID, String orderID, String method) {
        log.info("savePayment(paumentID={}, paymentDate={}, transactionID={}, amount={}, status={}, customerID={}, orderID={}, method={})", 
            paymentID, paymentDate, transactionID, amount, status, customerID, orderID, method);
        if(paymentID==null) return false;
        String paymentMethod = method.toLowerCase();
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentID);
        if(optionalPayment.isPresent()) return false;
        paymentRepository.saveFullPayment(paymentID, paymentDate, amount, 
            transactionID, customerID, orderID, status, paymentMethod);
        return true;
    }

    // update payment status by orderid
    private Boolean updatePaymentByOrderIDStatus(String orderID, PaymentStatusConstant status) throws PaymentNotFoundException {
        log.info("updatePaymentByOrderIDStatus(orderID={}, status={})", orderID, status);
        if(orderID==null && status==null) return false;
        Payment payment = paymentRepository.findByOrderID(orderID)
            .orElseThrow(()-> new PaymentNotFoundException("Payment of order " + orderID + " does not found"));
        paymentRepository.updatePaymentStatus(payment.getPaymentID(), status.name().toLowerCase());
        return true;
    }

    @Override
    public Boolean updatePaymentAsSuccessfulByOrderIDPerMethod(String orderID)
            throws PaymentNotFoundException {
        log.info("updatePaymentAsSuccessfulByOrderIDPerMethod(orderID={})", orderID);
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodByOrderID(orderID);
        PaymentMethodConstant method = PaymentMethodConstant.fromString(paymentMethod.getMethodName());
        switch (method) {
            case COD:
                return this.updatePaymentByOrderIDStatus(orderID, PaymentStatusConstant.SUCCESSFUL);
            case PAYPAL:
                return true;
            case MOMO:
                return true;
            case ZALOPAY:
                return true;
            default:
                break;
        }
        return false;
        
    }

    @Override
    public Boolean updatePaymentAsCanceledByOrderIDPerMethod(String orderID)
            throws PaymentNotFoundException {
        log.info("updatePaymentAsCanceledByOrderIDPerMethod(orderID={})", orderID);
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodByOrderID(orderID);
        PaymentMethodConstant method = PaymentMethodConstant.fromString(paymentMethod.getMethodName());
        switch (method) {
            case COD:
                return this.updatePaymentByOrderIDStatus(orderID, PaymentStatusConstant.CANCELLED);
            case PAYPAL:
                return this.updatePaymentByOrderIDStatus(orderID, PaymentStatusConstant.REFUND);
            case MOMO:
                return this.updatePaymentByOrderIDStatus(orderID, PaymentStatusConstant.REFUND);
            case ZALOPAY:
                return this.updatePaymentByOrderIDStatus(orderID, PaymentStatusConstant.REFUND);
            default:
                break;
        }
        return false;
    }

    @Override
    public Boolean updatePaymentStatusByOrderID(String orderID, PaymentStatusConstant status) throws PaymentNotFoundException {
        log.info("updatePaymentStatusByOrderID(orderID={}, status={})", orderID, status);
        return this.updatePaymentByOrderIDStatus(orderID, status);
    }

    @Override
    public PaymentDetails getPaymentDetails(String orderID) {
        log.info("getPaymentDetails(orderID={})", orderID);
        return paymentDetailsRepository.findByOrderID(orderID).orElseThrow(
            ()-> new NotFoundException("Payment of order " + orderID +" not found")
        );  
    }
    
}
