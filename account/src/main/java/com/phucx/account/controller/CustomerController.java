package com.phucx.account.controller;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.config.WebSocketConfig;
import com.phucx.account.constant.Notification;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.Customers;
import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderItem;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.customers.CustomersService;
import com.phucx.account.service.discounts.DiscountService;
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
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private DiscountService discountService;

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

    @GetMapping("/discount/validate")
    public ResponseEntity<ResponseFormat> validateDiscount(
        @RequestBody OrderItem orderItem) throws InvalidDiscountException{
        Boolean status = discountService.validateDiscountsOfProduct(orderItem);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @MessageMapping("/placeOrder")
    @SendTo("/topic/order")
    public OrderWithProducts placeOrder(Authentication authentication, @RequestBody OrderWithProducts order
    ) throws InvalidDiscountException, NotFoundException, RuntimeException, SQLException, InvalidOrderException{
        String username = usersService.getUsername(authentication);
        Customers customer = customersService.getCustomerDetail(username);
        logger.info("Customer {} has placed an order: {}",username, order.toString());
        if(customer!=null){
            order.setCustomerID(customer.getCustomerID());
            return customersService.placeOrder(order);
        }
        throw new NotFoundException("Customer is not found");
    }


    @MessageExceptionHandler(value = SQLException.class)
    public void handleSqlMessageException(Authentication authentication){
        String username = usersService.getUsername(authentication);
        Customers customer = customersService.getCustomerDetail(username);
        NotificationMessage notificationMessage = new NotificationMessage(
            "Error during processing order", Notification.CANCEL);
        simpMessagingTemplate.convertAndSendToUser(customer.getCustomerID(), 
            WebSocketConfig.QUEUE_MESSAGES, notificationMessage);
    }
    @MessageExceptionHandler(value = InvalidDiscountException.class)
    public void handleInvalidDiscountMessageException(Authentication authentication){
        String username = usersService.getUsername(authentication);
        Customers customer = customersService.getCustomerDetail(username);
        NotificationMessage notificationMessage = new NotificationMessage(
            "Invalid discount", Notification.CANCEL);
        simpMessagingTemplate.convertAndSendToUser(customer.getCustomerID(), 
            WebSocketConfig.QUEUE_MESSAGES, notificationMessage);
    }

    @MessageExceptionHandler(value = InvalidOrderException.class)
    public void handleInvalidOrderMessageException(Authentication authentication){
        String username = usersService.getUsername(authentication);
        Customers customer = customersService.getCustomerDetail(username);
        NotificationMessage notificationMessage = new NotificationMessage(
            "Invalid order", Notification.CANCEL);
        simpMessagingTemplate.convertAndSendToUser(customer.getCustomerID(), 
            WebSocketConfig.QUEUE_MESSAGES, notificationMessage);
    }
}
