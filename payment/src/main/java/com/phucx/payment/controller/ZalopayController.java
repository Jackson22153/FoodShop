package com.phucx.payment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.payment.service.paymentHandler.ZaloPayHandlerService;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/zalopay")
public class ZalopayController {
    @Autowired
    private ZaloPayHandlerService zaloPayHandlerService;
    @Value("${phucx.payment-successful-url}")
    private String successfulUrl;

    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestBody String jsonStr) {
        
        String result = zaloPayHandlerService.callback(jsonStr);
        return ResponseEntity.ok().body(result);
    }
    
    @GetMapping("pay/successful")
    public ResponseEntity<String> successful(@RequestParam("orderId") String orderId){
        Boolean result = zaloPayHandlerService.paymentSuccessfully(orderId);
        if(!result){
            log.error("Error while saving payment: {}", orderId);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(successfulUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
