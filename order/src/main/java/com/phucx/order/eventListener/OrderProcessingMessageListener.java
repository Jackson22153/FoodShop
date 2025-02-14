package com.phucx.order.eventListener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.constant.NotificationStatus;
import com.phucx.constant.NotificationTitle;
import com.phucx.constant.NotificationTopic;
import com.phucx.model.OrderNotificationDTO;
import com.phucx.order.annotations.LoggerAspect;
import com.phucx.order.config.MessageQueueConfig;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InSufficientInventoryException;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.ResponseFormat;
import com.phucx.order.service.customer.CustomerService;
import com.phucx.order.service.notification.NotificationService;
import com.phucx.order.service.order.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.ORDER_PROCESSING_QUEUE)
public class OrderProcessingMessageListener {
    @Autowired
    private OrderService  orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private NotificationService notificationService;

    @LoggerAspect
    @RabbitHandler
    public void orderProcessing(String message){
        log.info("orderProcessing(message={})", message);
        OrderNotificationDTO notification = new OrderNotificationDTO();
        notification.setTitle(NotificationTitle.CONFIRM_ORDER);
        notification.setTopic(NotificationTopic.Order);
        try {
            OrderWithProducts order = objectMapper.readValue(message, OrderWithProducts.class);
            Customer user = customerService.getCustomerByID(order.getCustomerID());
            try {
                notification.setOrderID(order.getOrderID());
                notification = this.validateOrder(order, notification, user);
            } catch (RuntimeException | NotFoundException e) {
                log.warn("Error: {}", e.getMessage());
                exceptionHandler(order, user.getUserID(), "Order #" + order.getOrderID() + 
                    " has been canceled due to " + e.getMessage(), notification);
            } catch (InvalidDiscountException e){
                log.warn("Error: Discount is invalid {}", e.getMessage());
                exceptionHandler(order, user.getUserID(), "Order #" + order.getOrderID() + 
                    " has been canceled due to invalid discount", notification);
            } catch(InvalidOrderException e){
                log.warn("Error: Order is invalid {}", e.getMessage()); 
                exceptionHandler(order, user.getUserID(), "Order #" + order.getOrderID() + 
                    " has been canceled due to invalid order", notification);
            } catch (InSufficientInventoryException e){
                log.warn("Error: Order is invalid due to {}", e.getMessage());
                exceptionHandler(order, user.getUserID(), e.getMessage(), notification);
            }
            notificationService.sendCustomerOrderNotification(notification);
        } catch (JsonProcessingException | NotFoundException e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    // validate order product's stocks
    @LoggerAspect
    private OrderNotificationDTO validateOrder(OrderWithProducts order, OrderNotificationDTO notification, Customer customer) 
        throws JsonProcessingException, InvalidDiscountException, InvalidOrderException, NotFoundException, InSufficientInventoryException{

        // Notification notification = new Notification();
        notification.setTitle(NotificationTitle.CONFIRM_ORDER);
        notification.setTopic(NotificationTopic.Order);
        if(!orderService.isPendingOrder(order.getOrderID())){
            throw new InvalidOrderException("Order " + order.getOrderID() + 
                " is not a pending order");
        }
        if(order.getEmployeeID()==null || order.getCustomerID()==null){
            throw new InvalidOrderException("Order " + order.getOrderID() + 
                " is invalid due to missing customer or employee");
        }
        // update employeeID for order
        boolean employeeUpdateCheck = orderService.updateOrderEmployee(order.getOrderID(), order.getEmployeeID());
        if(employeeUpdateCheck){
            // validate and update product instock
            ResponseFormat check = orderService.validateAndProcessOrder(order);
            if(!check.getStatus()){
                throw new RuntimeException(check.getError());
            }
            // notification
            notification.setMessage("Order #"+ order.getOrderID() +" has been confirmed");
            notification.setStatus(NotificationStatus.SUCCESSFUL);
            notification.setReceiverID(customer.getUserID());
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

    private void exceptionHandler(OrderWithProducts order, String userID, String errorMessage, OrderNotificationDTO notification
    ) throws JsonProcessingException{
        try {
            // notification
            notification.setTitle(NotificationTitle.CANCEL_ORDER);
            notification.setMessage(errorMessage);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setReceiverID(userID);
            // update order status
            orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
        } catch (NotFoundException e) {
            log.error("Error: {}", e.getMessage());
        }
    }

}
