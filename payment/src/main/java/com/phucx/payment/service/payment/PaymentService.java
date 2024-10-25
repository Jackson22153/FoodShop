package com.phucx.payment.service.payment;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paypal.base.rest.PayPalRESTException;
import com.phucx.model.PaymentDTO;
import com.phucx.payment.exception.NotFoundException;
import com.phucx.payment.exception.PaymentNotFoundException;
import com.phucx.payment.model.PaymentPerMonth;
import com.phucx.payment.model.PaymentPercentage;
import com.phucx.payment.model.PaymentResponse;


public interface PaymentService {
    public PaymentResponse createPayment(PaymentDTO paymentDTO) throws PayPalRESTException, JsonProcessingException, NotFoundException;
    public PaymentResponse updatePaymentByOrderIDAsSuccesful(String orderID) throws PaymentNotFoundException;
    public PaymentResponse updatePaymentByOrderIDAsCanceled(String orderID) throws PaymentNotFoundException;

    public List<PaymentPerMonth> getRevenuePerMonth(Integer year);
    public List<PaymentPercentage> getPaymentPercentageByYear(Integer year);
    public List<Integer> getPaymentYears();
}
