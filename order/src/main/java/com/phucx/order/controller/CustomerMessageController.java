package com.phucx.order.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.order.annotations.LoggerAspect;
import com.phucx.order.constant.NotificationStatus;
import com.phucx.order.constant.NotificationTopic;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderDetailsDTO;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Topic;
import com.phucx.order.service.customer.CustomerService;
import com.phucx.order.service.messageQueue.sender.MessageSender;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@MessageMapping("/customer")
public class CustomerMessageController {
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private CustomerService customerService;
    
    // ENDPOINT TO PLACE AN ORDER
    @LoggerAspect
    @MessageMapping("/order/placeOrder")
    @SendTo("/topic/order")
    public OrderDetailsDTO placeOrder(Authentication authentication, @RequestBody OrderWithProducts order
    ) throws InvalidDiscountException, NotFoundException, RuntimeException, SQLException, InvalidOrderException{
        OrderDetailsDTO pendingOrder = customerService.placeOrder(order, authentication);
        return pendingOrder;
    }

    @LoggerAspect
    @MessageMapping("/order/receive")
    public void receiveOrder(@RequestBody OrderWithProducts order, Authentication authentication){
        log.info("receiveOrder(orderID={}, userID={})", order.getOrderID(), authentication.getName());
        Notification notificationMessage = customerService.receiveOrder(order);

        log.info("receiver order: {}", notificationMessage);

        // send message to employee
        messageSender.sendNotification(notificationMessage);
        // send message to customer
        // notificationMessage.setReceiverID(userService.getUserIdOfEmployeeID(order.getEmployeeID()));
        // messageSender.sendNotification(notificationMessage);
    }

    // HANDLE MESSAGE EXCEPTION
    @MessageExceptionHandler(value = SQLException.class)
    public void handleSqlMessageException(Authentication authentication, Exception exception){
        log.warn("Error: {}", exception.getMessage());
        // notification
        Notification notificationMessage = new Notification(
            "Error order",
            "Error during processing order", null, authentication.getName(),
            new Topic(NotificationTopic.Order.name()), NotificationStatus.ERROR.name());
        messageSender.sendNotification(notificationMessage);
    }
    @MessageExceptionHandler(value = InvalidDiscountException.class)
    public void handleInvalidDiscountMessageException(Authentication authentication, Exception exception){
        log.warn("Error: {}", exception.getMessage());
        Notification notificationMessage = new Notification(
            "Invalid Order",
            "Invalid Discount", null, authentication.getName(),
            new Topic(NotificationTopic.Order.name()), NotificationStatus.ERROR.name());
        messageSender.sendNotification(notificationMessage);
    }

    @MessageExceptionHandler(value = InvalidOrderException.class)
    public void handleInvalidOrderMessageException(Authentication authentication, Exception exception){
        log.warn("Error: {}", exception.getMessage());
        Notification notificationMessage = new Notification(
            "Invalid Order",
            "Invalid order", null, authentication.getName(),
            new Topic(NotificationTopic.Order.name()), NotificationStatus.ERROR.name());
        messageSender.sendNotification(notificationMessage);
    }

    @MessageExceptionHandler(value = RuntimeException.class)
    public void handleRuntimeMessageException(Authentication authentication, Exception exception){
        log.warn("Error: {}", exception.getMessage());
        Notification notificationMessage = new Notification(
            "Invalid Order",
            "Error during processing order", null, authentication.getName(),
            new Topic(NotificationTopic.Order.name()), NotificationStatus.ERROR.name());
        messageSender.sendNotification(notificationMessage);
    }
}
