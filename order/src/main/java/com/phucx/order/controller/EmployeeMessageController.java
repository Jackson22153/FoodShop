// package com.phucx.order.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;

// import com.phucx.order.annotations.LoggerAspect;
// import com.phucx.order.exception.InvalidOrderException;
// import com.phucx.order.model.Notification;
// import com.phucx.order.model.OrderWithProducts;
// import com.phucx.order.service.employee.EmployeeService;
// import com.phucx.order.service.messageQueue.MessageQueueService;
// import com.phucx.order.service.user.UserService;

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
//     private MessageQueueService messageQueueService;   
//     // CONFIRM AN ORDER
//     @LoggerAspect
//     @MessageMapping("/order/confirm")
//     public void confirmOrder(@RequestBody OrderWithProducts order, Authentication authentication) throws InvalidOrderException{
//         log.info("confirmOrder(order={}, userID={})", order, authentication.getName());
//         // // validate order
//         // set employeeID that validates this order
//         String username = userService.getUsername(authentication);
//         EmployeeAccount employee = employeeService.getEmployeeAccount(username);
//         // get notification after validate with database
//         Notification notification = employeeService.confirmOrder(order, employee.getEmployeeID());
//         // send notification message back to customer
//         messageQueueService.sendNotification(notification);
//     }
//     // CANCEL AN ORDER
//     @LoggerAspect
//     @MessageMapping("/order/cancel")
//     public void cancelOrder(@RequestBody OrderWithProducts order, Authentication authentication
//     ){
//         log.info("cancelOrder(order={}, userID={})", order, authentication.getName());
//         // set employee to order
//         String username = userService.getUsername(authentication);
//         EmployeeAccount employee = employeeService.getEmployeeAccount(username);
//         order.setEmployeeID(employee.getEmployeeID());
//         // cancel order
//         Notification notificationMessage = employeeService.cancelOrder(order, employee.getEmployeeID());
//         // send message to customer
//         messageQueueService.sendNotification(notificationMessage);
//     }
//     // FULFILL ORDER
//     @LoggerAspect
//     @MessageMapping("/order/fulfill")
//     public void fulfillOrder(@RequestBody OrderWithProducts order, Authentication authentication){
//         log.info("fulfillOrder(order={}, userID={})", order.getOrderID(), authentication.getName());
//         // update order status
//         Notification notificationMessage = employeeService.fulfillOrder(order);
//         messageQueueService.sendNotification(notificationMessage);
//     }
// }
