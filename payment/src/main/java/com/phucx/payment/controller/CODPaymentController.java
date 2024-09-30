package com.phucx.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.payment.model.ResponseFormat;
import com.phucx.payment.service.paymentHandler.CODHandlerService;

@RestController
@RequestMapping("cod")
public class CODPaymentController {
    @Autowired
    private CODHandlerService codHandlerService;

    @PostMapping("pay/cancel")
    public ResponseEntity<ResponseFormat> cancel(@RequestParam("paymentId") String paymentId){
        Boolean status = codHandlerService.paymentCancelled(paymentId);

        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setStatus(status);
        responseFormat.setMessage("Payment " + paymentId + " has been canceled!");
        return ResponseEntity.ok().body(responseFormat);
        
    }

    @PostMapping("pay/successful")
    public ResponseEntity<ResponseFormat> successful(@RequestParam("paymentId") String paymentId){
        Boolean status = codHandlerService.paymentSuccessfully(paymentId);
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setStatus(status);
        responseFormat.setMessage("Payment " + paymentId + " has been canceled!");
        return ResponseEntity.ok().body(responseFormat);
    }
}
