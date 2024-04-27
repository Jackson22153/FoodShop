package com.phucx.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.annotations.LoggerAspect;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.employees.EmployeesService;
import com.phucx.account.service.messageQueue.sender.MessageSender;
import com.phucx.account.service.user.UserService;

@RestController
@RequestMapping("employee")
public class EmployeeController {
    private Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private EmployeesService employeesService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSender messageSender;
    // GET EMPLOYEE'S INFORMATION
    @GetMapping("info")
    public ResponseEntity<EmployeeDetail> getUserInfo(Authentication authentication){
        String username = userService.getUsername(authentication);
        logger.info("username: {}", username);
        EmployeeDetail employee = employeesService.getEmployeeDetail(username);
        return ResponseEntity.ok().body(employee);
    }
    // UPDATE EMPLOYEE'S INFORMATION
    @PostMapping("info")
    public ResponseEntity<ResponseFormat> updateUserInfo(
        Authentication authentication,
        @RequestBody EmployeeDetail employee
    ){
        Boolean status = employeesService.updateEmployeeInfo(employee);
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
        EmployeeDetail employee = employeesService.getEmployeeDetail(username);
        // get order's status
        OrderStatus status = null;
        if(orderStatus==null){
            status = OrderStatus.All;
        }else {
            status = OrderStatus.fromString(orderStatus.toUpperCase());
        }
        // get orders
        Page<OrderDetailsDTO> orders = employeesService.getOrders(
            pageNumber, WebConstant.PAGE_SIZE, employee.getEmployeeID(), status);
        return ResponseEntity.ok().body(orders);
    }

    // GET ALL AVAIABLE PENDING ORDERS 
    @GetMapping("/orders/pending")
    public ResponseEntity<Page<OrderDetailsDTO>> getPendingOrders(
        @RequestParam(name = "page", required = false) Integer page,
        Authentication authentication
    ){
        page = page!=null?page:0;
        Page<OrderDetailsDTO> orders = employeesService.getPendingOrders(page, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(orders);
    }
    
    // GET PENDING ORDER DETAIL
    @GetMapping("/orders/pending/{orderID}")
    public ResponseEntity<OrderWithProducts> getPendingOrder(
        @PathVariable(name = "orderID") Integer orderID,
        Authentication authentication
    ) throws InvalidOrderException{
        OrderWithProducts orders = employeesService.getPendingOrderDetail(orderID);
        return ResponseEntity.ok().body(orders);
    }
    
    // GET EMPLOYEE'S APPROVED ORDERS
    @GetMapping("/orders/{orderID}")
    public ResponseEntity<OrderWithProducts> getOrderDetail(@PathVariable Integer orderID, Authentication authentication) 
    throws InvalidOrderException{
        String username = userService.getUsername(authentication);
        EmployeeDetail employee = employeesService.getEmployeeDetail(username);
        OrderWithProducts order = employeesService.getOrderDetail(orderID, employee.getEmployeeID());
        return ResponseEntity.ok().body(order);
    }
    
    // CONFIRM AN ORDER
    @LoggerAspect
    @MessageMapping("/order.validate")
    public void validateOrder(@RequestBody OrderWithProducts order,
        Authentication authentication
    ){
        // // validate order
        // set employeeID that validates this order
        String username = userService.getUsername(authentication);
        EmployeeDetail employee = employeesService.getEmployeeDetail(username);
        order.setEmployeeID(employee.getEmployeeID());

        logger.info("employee {} has validated an order of customer {}", 
            order.getEmployeeID(), order.getCustomerID());
        // get notification after validate with database
        NotificationMessage notificationMessage = employeesService.placeOrder(order);
        
        // send notification message back to customer
        // get recipientID to send to a specific user
        String recipientID = userService.getUserIdOfCustomerID(order.getCustomerID());
        logger.info("recipientID: {}", recipientID);
        logger.info("notification: {}", notificationMessage.toString());
        messageSender.sendMessageToUser(recipientID, notificationMessage);
    }
}
