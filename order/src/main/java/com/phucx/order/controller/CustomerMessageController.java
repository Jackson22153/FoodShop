package com.phucx.order.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.annotations.LoggerAspect;
import com.phucx.order.constant.NotificationStatus;
import com.phucx.order.constant.NotificationTopic;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Topic;
import com.phucx.order.service.messageQueue.MessageQueueService;
import com.phucx.order.service.order.CustomerOrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@MessageMapping("/customer")
public class CustomerMessageController {
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private CustomerOrderService customerOrderService;
    
    // ENDPOINT TO PLACE AN ORDER
    @LoggerAspect
    @MessageMapping("/order/placeOrder")
    @SendTo("/topic/order")
    public OrderDetails placeOrder(@RequestBody OrderWithProducts order, Authentication authentication) 
        throws JsonProcessingException, InvalidDiscountException, InvalidOrderException{

        OrderDetails pendingOrder = customerOrderService.placeOrder(order, authentication.getName());
        return pendingOrder;
    }

    @LoggerAspect
    @MessageMapping("/order/receive")
    public void receiveOrder(@RequestBody OrderWithProducts order, Authentication authentication) 
        throws JsonProcessingException{
        customerOrderService.receiveOrder(order);
    }

    // HANDLE MESSAGE EXCEPTION
    @MessageExceptionHandler(value = SQLException.class)
    public void handleSqlMessageException(Authentication authentication, Exception exception){
        log.warn("Error: {}", exception.getMessage());
        try {
            Notification notificationMessage = new Notification(
                "Error order",
                "Error during processing order", null, authentication.getName(),
                new Topic(NotificationTopic.Order.name()), NotificationStatus.ERROR.name());
            messageQueueService.sendNotification(notificationMessage);
        } catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
        }
    }
    @MessageExceptionHandler(value = InvalidDiscountException.class)
    public void handleInvalidDiscountMessageException(Authentication authentication, Exception exception){
        log.warn("Error: {}", exception.getMessage());
        try {
            Notification notificationMessage = new Notification(
                "Invalid Order",
                "Invalid Discount", null, authentication.getName(),
                new Topic(NotificationTopic.Order.name()), NotificationStatus.ERROR.name());
            messageQueueService.sendNotification(notificationMessage);
        } catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    @MessageExceptionHandler(value = InvalidOrderException.class)
    public void handleInvalidOrderMessageException(Authentication authentication, Exception exception){
        log.warn("Error: {}", exception.getMessage());
        try {
            Notification notificationMessage = new Notification(
                "Invalid Order",
                "Invalid order", null, authentication.getName(),
                new Topic(NotificationTopic.Order.name()), NotificationStatus.ERROR.name());
            messageQueueService.sendNotification(notificationMessage);
        } catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    @MessageExceptionHandler(value = RuntimeException.class)
    public void handleRuntimeMessageException(Authentication authentication, Exception exception){
        log.warn("Error: {}", exception.getMessage());
        try {
            Notification notificationMessage = new Notification(
                "Invalid Order",
                "Error during processing order", null, authentication.getName(),
                new Topic(NotificationTopic.Order.name()), NotificationStatus.ERROR.name());
            messageQueueService.sendNotification(notificationMessage);
        } catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}
