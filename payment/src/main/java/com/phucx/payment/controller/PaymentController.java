package com.phucx.payment.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.base.rest.PayPalRESTException;
import com.phucx.model.PaymentDTO;
import com.phucx.payment.exception.NotFoundException;
import com.phucx.payment.model.PaymentMethod;
import com.phucx.payment.model.PaymentResponse;
import com.phucx.payment.service.payment.PaymentProcessorService;
import com.phucx.payment.service.paymentMethod.PaymentMethodService;
import com.phucx.payment.utils.ServerUrlUtils;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PaymentController {
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private PaymentProcessorService paymentProcessorService;

    @Operation(summary = "Pay order", tags = {"payment", "post", "customer"})
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> payment(@RequestBody PaymentDTO payment, 
        HttpServletRequest request, HttpServletResponse response
    ) throws PayPalRESTException, IOException, NotFoundException{
        String baseUrl = ServerUrlUtils.getBaseUrl(request);
        payment.setBaseUrl(baseUrl);
        PaymentResponse paymentResponse = paymentProcessorService.createPayment(payment);
        return ResponseEntity.ok().body(paymentResponse);
    }

    @Operation(summary = "Get payment methods", tags = {"payment", "get"})
    @GetMapping("/methods")
    public ResponseEntity<List<PaymentMethod>> getPaymentMethods(){
        List<PaymentMethod> methods = paymentMethodService.getPaymmentMethods();
        return ResponseEntity.ok().body(methods);
    }
}
