package com.phucx.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.model.Customers;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.customers.CustomersService;

@RestController
@RequestMapping("customer")
public class CustomerController {
    private Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomersService customersService;
    // @GetMapping("/info")
    // public String userOIcd(Authentication authentication){
    //     Jwt jwt = (Jwt) authentication.getPrincipal();
    //     return authentication.getName();
    // }

    // @GetMapping("{username}")
    // public ResponseEntity<Users> getUserAccount(@PathVariable String username){
    //     Users user = usersService.getUser(username);
    //     return ResponseEntity.ok().body(user);
    // }

    @GetMapping("info")
    public ResponseEntity<Customers> getUserInfo(Authentication authentication){
        String userID = authentication.getName();
        Customers customer = customersService.getCustomerDetail(userID);
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

    
}