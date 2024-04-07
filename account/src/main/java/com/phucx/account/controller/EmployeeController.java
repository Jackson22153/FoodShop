package com.phucx.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.config.WebConfig;
import com.phucx.account.config.WebSocketConfig;
import com.phucx.account.model.Employees;
import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.employees.EmployeesService;
import com.phucx.account.service.users.UsersService;

@RestController
@RequestMapping("employee")
public class EmployeeController {
    private Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private EmployeesService employeesService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @GetMapping("info")
    public ResponseEntity<Employees> getUserInfo(Authentication authentication){
        String username = usersService.getUsername(authentication);
        logger.info("username: {}", username);
        Employees employee = employeesService.getEmployeeDetail(username);
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("info")
    public ResponseEntity<ResponseFormat> updateUserInfo(
        Authentication authentication,
        @RequestBody Employees employee
    ){
        // String userID = authentication.getName();
        // customer.setCustomerID(userID);
        boolean check = employeesService.updateEmployeeInfo(employee);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("order")
    public ResponseEntity<ResponseFormat> placeOrder(
        Authentication authentication,
        @RequestBody OrderWithProducts order
    ){
        logger.info(order.toString());
        String username = usersService.getUsername(authentication);
        Employees employees = employeesService.getEmployeeDetail(username);
        order.setEmployeeID(employees.getEmployeeID());
        int orderID = employeesService.placeOrder(order);
        logger.info("employee {} has validated an order {} for customer {}", username, orderID, order.getCustomerID());
        return ResponseEntity.ok().body(new ResponseFormat(true));
    }

    @MessageMapping("/order.validate")
    public void validateOrder(@RequestBody OrderWithProducts order,
        Authentication authentication
    ){
        logger.info("employee {} has placed an order", order.getEmployeeID());
        // validate order
        Integer orderIDResponse = sendOrder(order, authentication);
        // 
        NotificationMessage notificationMessage = new NotificationMessage();
        if(orderIDResponse!=null){
            notificationMessage.setContent("Your order has been placed successfully");
            notificationMessage.setStatus(WebConfig.SUCCESSFUL_NOTIFICATION);
        }else{
            notificationMessage.setContent("Your order has been canceled");
            notificationMessage.setStatus(WebConfig.FAILED_NOTIFICATION);
        }
        // websocket 
        // sending notify message to customer whose order is placed 
        String customerID = order.getCustomerID();
        this.simpMessagingTemplate.convertAndSendToUser(customerID, WebSocketConfig.QUEUE_MESSAGES, 
            notificationMessage);
    }

    private Integer sendOrder(OrderWithProducts order, Authentication authentication){
        logger.info(order.toString());
        String username = usersService.getUsername(authentication);
        Employees employees = employeesService.getEmployeeDetail(username);
        order.setEmployeeID(employees.getEmployeeID());
        Integer orderID = employeesService.placeOrder(order);
        logger.info("employee {} has validated an order {} for customer {}", username, orderID, order.getCustomerID());
        return orderID;
    }
}
