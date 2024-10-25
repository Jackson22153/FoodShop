package com.phucx.payment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.payment.model.PaymentPerMonth;
import com.phucx.payment.model.PaymentPercentage;
import com.phucx.payment.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    
    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Get revenue per month by year", tags = {"get", "payment", "admin"})
    @GetMapping(value = "/revenue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PaymentPerMonth>> getRevenue(
        @RequestParam(name = "year") Integer year
    ){
        List<PaymentPerMonth> revenue = paymentService.getRevenuePerMonth(year);
        return ResponseEntity.ok().body(revenue);
    }

    @Operation(summary = "Get payment status percentage by year", tags = {"get", "payment", "admin"})
    @GetMapping(value = "/percentage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PaymentPercentage>> getPaymentPercentageByYear(
        @RequestParam(name = "year") Integer year
    ){
        List<PaymentPercentage> products = paymentService.getPaymentPercentageByYear(year);
        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "Get payment years", tags = {"get", "payment", "admin"})
    @GetMapping(value = "/years", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> getPaymentYears(){
        List<Integer> years = paymentService.getPaymentYears();
        return ResponseEntity.ok().body(years);
    }



}
