package com.phucx.payment.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.base.rest.PayPalRESTException;
import com.phucx.payment.service.paymentHandler.PaypalHandlerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("paypal")
public class PaypalPaymentController {
    @Autowired
    private PaypalHandlerService paypalService;
    @Value("${phucx.ui-url}")
    private String uiUrl;
    @Value("${phucx.payment-successful-url}")
    private String successfulUrl;
    @Value("${phucx.payment-canceled-url}")
    private String canceledUrl;


    @GetMapping("pay/cancel")
    public ResponseEntity<String> cancel(@RequestParam(name = "token") String token, 
        @RequestParam(name = "orderId") String orderId){

        Boolean status = paypalService.paymentCancelled(orderId);
        if(status){

        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(canceledUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("pay/successful")
    public ResponseEntity<String> successful(
        @RequestParam("paymentId") String paymentId, 
        @RequestParam("PayerID") String payerId) throws PayPalRESTException{
        
        Boolean result = paypalService.paymentSuccessfully(paymentId, payerId);
        if(!result){
            log.error("Error while saving payment: {}", paymentId);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(successfulUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
