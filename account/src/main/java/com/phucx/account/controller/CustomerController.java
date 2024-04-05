package com.phucx.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.config.WebConfig;
import com.phucx.account.model.Customers;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.model.UserOrderProducts;
import com.phucx.account.service.customers.CustomersService;
import com.phucx.account.service.messageQueue.sender.MessageSender;


@RestController
@RequestMapping("customer")
public class CustomerController {
    private Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomersService customersService;


    @GetMapping("info")
    public ResponseEntity<Customers> getUserInfo(Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userID = jwt.getSubject();
        String username = jwt.getClaimAsString(WebConfig.PREFERRED_USERNAME);
        logger.info("userID: {}, username: {}", userID, username);
        Customers customer = customersService.getCustomerDetail(username);
        return ResponseEntity.ok().body(customer);
    }

    @PostMapping("info")
    public ResponseEntity<ResponseFormat> updateUserInfo(
        Authentication authentication,
        @RequestBody Customers customer
    ){
        boolean check = customersService.updateCustomerInfo(customer);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("order")
    public ResponseEntity<ResponseFormat> placeOrder(
        @RequestBody UserOrderProducts userOrderProducts){
        boolean check = customersService.placeOrder(userOrderProducts);
        return ResponseEntity.ok().body(new ResponseFormat(check));
    }

    
}
