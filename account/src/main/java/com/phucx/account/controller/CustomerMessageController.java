// package com.phucx.account.controller;

// import java.sql.SQLException;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.SendTo;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;

// import com.phucx.account.annotations.LoggerAspect;
// import com.phucx.account.constant.NotificationStatus;
// import com.phucx.account.constant.NotificationTopic;
// import com.phucx.account.constant.OrderStatus;
// import com.phucx.account.exception.InvalidDiscountException;
// import com.phucx.account.exception.InvalidOrderException;
// import com.phucx.account.model.Customer;
// import com.phucx.account.model.Notification;
// import com.phucx.account.model.OrderDetailsDTO;
// import com.phucx.account.model.OrderWithProducts;
// import com.phucx.account.model.Topic;
// import com.phucx.account.service.customer.CustomerService;
// import com.phucx.account.service.messageQueue.sender.MessageSender;
// import com.phucx.account.service.order.OrderService;
// import com.phucx.account.service.user.UserService;

// import jakarta.ws.rs.NotFoundException;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @RestController
// @MessageMapping("/customer")
// public class CustomerMessageController {
//     @Autowired
//     private MessageSender messageSender;
//     @Autowired
//     private UserService userService;
//     @Autowired
//     private CustomerService customerService;
//     @Autowired
//     private OrderService orderService;
//     // ENDPOINT TO PLACE AN ORDER
//     @LoggerAspect
//     @MessageMapping("/order/placeOrder")
//     @SendTo("/topic/order")
//     public OrderDetailsDTO placeOrder(Authentication authentication, @RequestBody OrderWithProducts order
//     ) throws InvalidDiscountException, NotFoundException, RuntimeException, SQLException, InvalidOrderException{
//         log.info("placeOrder(userID={}, order{})", authentication.getName(), order.toString());
//         String username = userService.getUsername(authentication);
//         Customer customer = customerService.getCustomerByUsername(username);
//         log.info("Customer {} has placed an order: {}",username, order.toString());
//         if(customer!=null){
//             order.setCustomerID(customer.getCustomerID());
//             OrderWithProducts newOrder = customerService.placeOrder(order);
//             if(newOrder !=null){
//                 // create and save notification back to user
//                 Notification notificationToUser = new Notification(
//                     "Place Order",
//                     "Order #"+newOrder.getOrderID()+" has been placed successfully",
//                     null, authentication.getName(), new Topic(NotificationTopic.Order.name()),
//                     NotificationStatus.SUCCESSFUL.name(), true);
//                 // send message back to user
//                 messageSender.sendMessageToUser(customer.getUser().getUserID(), notificationToUser);
//                 // send message to notification message queue
//                 Notification notificationToTopic = new Notification(
//                     "Place Order",
//                     "Order #" + newOrder.getOrderID() + " has been placed", 
//                     authentication.getName(), null, new Topic(NotificationTopic.Order.name()), 
//                     NotificationStatus.SUCCESSFUL.name(), true);
//                 messageSender.sendNotification(notificationToTopic);
//                 // 
//                 return orderService.getOrder(newOrder.getOrderID(), OrderStatus.Pending);
//             }
//             throw new RuntimeException("Error when placing an order");
//         }
//         throw new NotFoundException("Customer is not found");
//     }

//     @LoggerAspect
//     @MessageMapping("/order/receive")
//     public void receiveOrder(@RequestBody OrderWithProducts order, Authentication authentication){
//         log.info("receiveOrder(orderID={}, userID={})", order.getOrderID(), authentication.getName());
//         Notification notificationMessage = customerService.receiveOrder(order);

//         log.info("receiver order: {}", notificationMessage);

//         // send message to employee
//         messageSender.sendNotification(notificationMessage);
//         // send message to customer
//         // notificationMessage.setReceiverID(userService.getUserIdOfEmployeeID(order.getEmployeeID()));
//         // messageSender.sendNotification(notificationMessage);
//     }

//     // HANDLE MESSAGE EXCEPTION
//     @MessageExceptionHandler(value = SQLException.class)
//     public void handleSqlMessageException(Authentication authentication, Exception exception){
//         log.warn(exception.getMessage());
//         // notification
//         Notification notificationMessage = new Notification(
//             "Error order",
//             "Error during processing order", null, authentication.getName(),
//             new Topic(NotificationTopic.Order.name()), NotificationStatus.ERROR.name());
//         messageSender.sendNotification(notificationMessage);
//     }
//     @MessageExceptionHandler(value = InvalidDiscountException.class)
//     public void handleInvalidDiscountMessageException(Authentication authentication, Exception exception){
//         log.info(exception.getMessage());
//         Notification notificationMessage = new Notification(
//             "Invalid Order",
//             "Invalid Discount", null, authentication.getName(),
//             new Topic(NotificationTopic.Order.name()), NotificationStatus.ERROR.name());
//         messageSender.sendNotification(notificationMessage);
//     }

//     @MessageExceptionHandler(value = InvalidOrderException.class)
//     public void handleInvalidOrderMessageException(Authentication authentication){
//         Notification notificationMessage = new Notification(
//             "Invalid Order",
//             "Invalid order", null, authentication.getName(),
//             new Topic(NotificationTopic.Order.name()), NotificationStatus.ERROR.name());
//         messageSender.sendNotification(notificationMessage);
//     }

//     @MessageExceptionHandler(value = RuntimeException.class)
//     public void handleRuntimeMessageException(Authentication authentication, Exception exception){
//         Notification notificationMessage = new Notification(
//             "Invalid Order",
//             "Error during processing order", null, authentication.getName(),
//             new Topic(NotificationTopic.Order.name()), NotificationStatus.ERROR.name());
//         messageSender.sendNotification(notificationMessage);
//     }
// }
