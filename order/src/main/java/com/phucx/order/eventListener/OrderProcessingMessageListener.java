package com.phucx.order.eventListener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.order.annotations.LoggerAspect;
import com.phucx.order.config.MessageQueueConfig;
import com.phucx.order.constant.NotificationStatus;
import com.phucx.order.constant.NotificationTopic;
import com.phucx.order.constant.NotificationTitle;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.OrderNotificationDTO;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.User;
import com.phucx.order.service.notification.NotificationService;
import com.phucx.order.service.order.OrderService;
import com.phucx.order.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.ORDER_PROCESSING_QUEUE)
public class OrderProcessingMessageListener {
    @Autowired
    private OrderService  orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private NotificationService notificationService;

    @LoggerAspect
    @RabbitHandler
    public void orderProcessing(String message){
        log.info("orderProcessing(message={})", message);
        OrderNotificationDTO notification = new OrderNotificationDTO();
        notification.setTitle(NotificationTitle.PLACE_ORDER);
        notification.setTopic(NotificationTopic.Order);
        try {
            OrderWithProducts order = objectMapper.readValue(message, OrderWithProducts.class);
            User user = userService.getUserByCustomerID(order.getCustomerID());
            try {
                notification.setOrderID(order.getOrderID());
                notification = this.validateOrder(order, notification, user);
            } catch (RuntimeException e) {
                log.warn("Error: {}", e.getMessage());
                // notification
                notification.setMessage("Order #"+order.getOrderID()+" has been canceled");
                notification.setStatus(NotificationStatus.FAILED);
                notification.setReceiverID(user.getUserID());
                // update order status
                orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
            } catch (InvalidDiscountException e){
                log.warn("Error: Discount is invalid {}", e.getMessage());
                // notification
                notification.setMessage("Order #"+order.getOrderID()+" has been canceled due to invalid discount");
                notification.setStatus(NotificationStatus.FAILED);
                notification.setReceiverID(user.getUserID());
                // update order status
                orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
            } catch(InvalidOrderException e){
                log.warn("Error: Order is invalid {}", e.getMessage());
                notification.setMessage("Order #"+order.getOrderID()+" has been canceled due to invalid order");
                notification.setStatus(NotificationStatus.FAILED);
                notification.setReceiverID(user.getUserID());
                // update order status
                orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
                }
            notificationService.sendNotification(notification);
        } catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    // validate order product's stocks
    @LoggerAspect
    private OrderNotificationDTO validateOrder(OrderWithProducts order, OrderNotificationDTO notification, User user) 
        throws JsonProcessingException, InvalidDiscountException, InvalidOrderException{

        // Notification notification = new Notification();
        notification.setTitle(NotificationTitle.PLACE_ORDER);
        notification.setTopic(NotificationTopic.Order);
        if(!orderService.isPendingOrder(order.getOrderID())){
            throw new InvalidOrderException("Order " + order.getOrderID() + " is not a pending order");
        }
        if(order.getEmployeeID()==null || order.getCustomerID()==null){
            throw new InvalidOrderException("Order " + order.getOrderID() + " is invalid due to missing customer or employee");
        }
        // update employeeID for order
        boolean employeeUpdateCheck = orderService.updateOrderEmployee(order.getOrderID(), order.getEmployeeID());
        if(employeeUpdateCheck){
            // validate and update product instock
            boolean check = orderService.validateAndProcessOrder(order);
            if(!check){
                throw new RuntimeException("Order has been canceled");
            }
            // notification
            notification.setMessage("Order #"+ order.getOrderID() +" has been confirmed");
            notification.setStatus(NotificationStatus.SUCCESSFUL);
            notification.setReceiverID(user.getUserID());
            // update order status
            orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Confirmed);

            // update read status for pending order after validating
            notificationService.markAsReadForConfirmedOrderNotification(notification);
        } else {
            log.warn("Can not update employeeID for order " + order.getOrderID());
            throw new RuntimeException("Can not update employeeID for order "+ order.getOrderID());
        }
        return notification;
    }
}
