package com.phucx.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.constant.OrderStatus;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.Notification;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.employee.EmployeeService;
import com.phucx.account.service.user.UserService;

@RestController
@RequestMapping("employee")
public class EmployeeController {
    private Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UserService userService;


    @GetMapping("/isEmployee")
    public ResponseEntity<ResponseFormat> isEmployee(){
        return ResponseEntity.ok().body(new ResponseFormat(true));
    }
    // GET EMPLOYEE'S INFORMATION
    @GetMapping("/info")
    public ResponseEntity<EmployeeDetail> getUserInfo(Authentication authentication){
        String username = userService.getUsername(authentication);
        EmployeeDetail employee = employeeService.getEmployeeDetail(username);
        return ResponseEntity.ok().body(employee);
    }
    // UPDATE EMPLOYEE'S INFORMATION
    @PostMapping("/info")
    public ResponseEntity<ResponseFormat> updateUserInfo(
        @RequestBody EmployeeDetail employee
    ){
        Boolean status = employeeService.updateEmployeeInfo(employee);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    // GET ALL ORDERS WHICH EMPLOYEE HAS APPROVED
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDetailsDTO>> getOrders(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "type", required = false) String orderStatus,
        Authentication authentication
    ){    
        logger.info("OrderStatus: {}", orderStatus);
        pageNumber = pageNumber!=null?pageNumber:0;
        String username = userService.getUsername(authentication);
        EmployeeDetail employee = employeeService.getEmployeeDetail(username);
        // get order's status
        OrderStatus status = null;
        if(orderStatus!=null){
            status = OrderStatus.fromString(orderStatus.toUpperCase());
        }else {
            status = OrderStatus.All;
        }
        // get orders
        Page<OrderDetailsDTO> orders = employeeService.getOrders(
            employee.getEmployeeID(), status,
            pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(orders);
    }

    // notification
    @GetMapping("/notifications")
    public ResponseEntity<Page<Notification>> getNotificationByReceiverID(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        Authentication authentication
    ){
        logger.info("getNotificationByReceiverID(page={}, userID={})", pageNumber, authentication.getName());
        pageNumber=pageNumber!=null?pageNumber:0;
        Page<Notification> notifications = employeeService.getNotifications(
            authentication.getName(), pageNumber, WebConstant.NOTIFICATION_PAGE_SIZE);
        return ResponseEntity.ok().body(notifications);
    }
    @PostMapping("/notifications")
    public ResponseEntity<ResponseFormat> turnOffNotification(
        @RequestBody Notification notification, Authentication authentication
    ){
        Boolean status = employeeService.turnOffNotification(
            notification.getNotificationID(), authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }
}
