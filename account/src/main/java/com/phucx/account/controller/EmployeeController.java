package com.phucx.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.constant.WebConstant;
import com.phucx.account.model.Employees;
import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.employees.EmployeesService;
import com.phucx.account.service.messageQueue.sender.MessageSender;
import com.phucx.account.service.order.OrderService;
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
    @Autowired
    private OrderService orderService;
    @Autowired
    private MessageSender messageSender;

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
        boolean check = employeesService.updateEmployeeInfo(employee);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/orders/pending")
    public ResponseEntity<Page<OrderWithProducts>> getPendingOrders(
        @RequestParam(name = "page", required = false) Integer page
    ){
        page = page!=null?page:0;
        return ResponseEntity.ok().body(orderService.getPendingOrders(page, WebConstant.PAGE_SIZE));
    }

    @MessageMapping("/order.validate")
    public void validateOrder(@RequestBody OrderWithProducts order,
        Authentication authentication
    ){
        // // validate order
        // set employeeID that validates this order
        String username = usersService.getUsername(authentication);
        Employees employees = employeesService.getEmployeeDetail(username);
        order.setEmployeeID(employees.getEmployeeID());

        logger.info("employee {} has validated an order of customer {}", 
            order.getEmployeeID(), order.getCustomerID());
        // get notification after validate with database
        NotificationMessage notificationMessage = employeesService.placeOrder(order);
        
        // send notification message back to customer
        // get recipientID to send to a specific user
        String recipientID = usersService.getUserIdOfCustomerID(order.getCustomerID());
        logger.info("recipientID: {}", recipientID);
        logger.info("notification: {}", notificationMessage.toString());
        messageSender.sendMessageToUser(recipientID, notificationMessage);
        // this.simpMessagingTemplate.convertAndSendToUser(
        //     order.getCustomerID(), WebSocketConfig.QUEUE_MESSAGES, 
        //     notificationMessage);
    }
}
