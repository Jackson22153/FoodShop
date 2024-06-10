package com.phucx.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.customer.CustomerService;
import com.phucx.account.service.user.UserService;

@RestController
@RequestMapping("customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;

    @GetMapping("/isCustomer")
    public ResponseEntity<ResponseFormat> isCustomer(){
        return ResponseEntity.ok().body(new ResponseFormat(true));
    }
    // GET CUSTOMER'S INFOMATION
    @GetMapping("/info")
    public ResponseEntity<CustomerDetail> getUserInfo(Authentication authentication){
        String username = userService.getUsername(authentication);
        CustomerDetail customer = customerService.getCustomerDetail(username);
        return ResponseEntity.ok().body(customer);
    }
    // UPDATE CUSTOMER'S INFOMATION
    @PostMapping("/info")
    public ResponseEntity<ResponseFormat> updateUserInfo(
        Authentication authentication,
        @RequestBody CustomerDetail customer
    ){
        boolean check = customerService.updateCustomerInfo(customer);
        return ResponseEntity.ok().body(new ResponseFormat(check));
    }
}
