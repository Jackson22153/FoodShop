// package com.phucx.account.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;

// import com.phucx.account.annotations.LoggerAspect;
// import com.phucx.account.exception.InvalidOrderException;
// import com.phucx.account.model.EmployeeDetail;
// import com.phucx.account.model.Notification;
// import com.phucx.account.model.OrderWithProducts;
// import com.phucx.account.service.employee.EmployeeService;
// import com.phucx.account.service.messageQueue.sender.MessageSender;
// import com.phucx.account.service.user.UserService;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @RestController
// @MessageMapping("/employee")
// public class EmployeeMessageController {
//     @Autowired
//     private UserService userService;
//     @Autowired
//     private EmployeeService employeeService;
//     @Autowired
//     private MessageSender messageSender;   
//     // CONFIRM AN ORDER
//     @LoggerAspect
//     @MessageMapping("/order/confirm")
//     public void confirmOrder(@RequestBody OrderWithProducts order, Authentication authentication) throws InvalidOrderException{
//         log.info("confirmOrder(order={}, userID={})", order, authentication.getName());
//         // // validate order
//         // set employeeID that validates this order
//         String username = userService.getUsername(authentication);
//         EmployeeDetail employee = employeeService.getEmployeeDetail(username);
//         // get notification after validate with database
//         Notification notification = employeeService.confirmOrder(order, employee.getEmployeeID());
//         // send notification message back to customer
//         messageSender.sendNotification(notification);
//     }
//     // CANCEL AN ORDER
//     @LoggerAspect
//     @MessageMapping("/order/cancel")
//     public void cancelOrder(@RequestBody OrderWithProducts order, Authentication authentication
//     ){
//         log.info("cancelOrder(order={}, userID={})", order, authentication.getName());
//         // set employee to order
//         String username = userService.getUsername(authentication);
//         EmployeeDetail employee = employeeService.getEmployeeDetail(username);
//         order.setEmployeeID(employee.getEmployeeID());
//         // cancel order
//         Notification notificationMessage = employeeService.cancelOrder(order, employee.getEmployeeID());
//         // send message to customer
//         messageSender.sendNotification(notificationMessage);
//     }
//     // FULFILL ORDER
//     @LoggerAspect
//     @MessageMapping("/order/fulfill")
//     public void fulfillOrder(@RequestBody OrderWithProducts order, Authentication authentication){
//         log.info("fulfillOrder(order={}, userID={})", order.getOrderID(), authentication.getName());
//         // update order status
//         Notification notificationMessage = employeeService.fulfillOrder(order);
//         messageSender.sendNotification(notificationMessage);
//     }
// }
