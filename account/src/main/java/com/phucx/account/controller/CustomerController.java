package com.phucx.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.model.Customers;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.customers.CustomersService;
import com.phucx.account.service.users.UsersService;

import jakarta.ws.rs.NotFoundException;


@RestController
@RequestMapping("customer")
public class CustomerController {
    private Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomersService customersService;
    @Autowired
    private UsersService usersService;

    @GetMapping("info")
    public ResponseEntity<Customers> getUserInfo(Authentication authentication){
        String username = usersService.getUsername(authentication);
        logger.info("username: {}", username);
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

    @MessageMapping("/placeOrder")
    @SendTo("/topic/order")
    public OrderWithProducts placeOrder(
        Authentication authentication,
        @RequestBody OrderWithProducts order
    ) throws InvalidDiscountException{

        String username = usersService.getUsername(authentication);
        Customers customer = customersService.getCustomerDetail(username);
        logger.info("Customer {} has placed an order: {}",username, order.toString());
        if(customer!=null){
            order.setCustomerID(customer.getCustomerID());
            return customersService.placeOrder(order);
        }
        throw new NotFoundException("Customer is not found");
    }

    
}
