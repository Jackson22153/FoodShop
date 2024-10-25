package com.phucx.payment.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/paypal", produces = MediaType.APPLICATION_JSON_VALUE)
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

        paypalService.paymentCancelled(orderId);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(canceledUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("pay/successful")
    public ResponseEntity<String> successful(
        @RequestParam("paymentId") String paymentId, 
        @RequestParam("PayerID") String payerId,
        @RequestParam("orderId") String orderId) throws PayPalRESTException{
        
        paypalService.paymentSuccessfully(paymentId, payerId, orderId);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(successfulUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
