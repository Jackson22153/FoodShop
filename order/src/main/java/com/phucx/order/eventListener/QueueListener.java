package com.phucx.order.eventListener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.phucx.order.annotations.LoggerAspect;
import com.phucx.order.config.MessageQueueConfig;
import com.phucx.order.constant.NotificationStatus;
import com.phucx.order.constant.NotificationTopic;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Topic;
import com.phucx.order.model.User;
import com.phucx.order.service.order.OrderService;
import com.phucx.order.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListeners({
    @RabbitListener(queues = MessageQueueConfig.ORDER_NOTIFICATION_QUEUE),
    @RabbitListener(queues = MessageQueueConfig.ORDER_QUEUE)
})
public class QueueListener {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    // @Autowired
    // private MessageQueueService messageQueueService;

    // @LoggerAspect
    // @RabbitHandler
    // public void notificationOrderReceiver(Notification notification){
    //     log.info("notificationReceiver: {}", notification);
    //     if(notification.getReceiverID()!=null){
    //         messageQueueService.sendMessageToUser(notification.getReceiverID(), notification);
    //     }else{
    //         messageQueueService.sendEmployeeNotificationOrderToTopic(notification);
    //     }
    // }

    @RabbitHandler
    public Notification orderReceiver(OrderWithProducts order){
        return validateOrder(order);
    }
    
    @LoggerAspect
    // validate order product's stocks
    private Notification validateOrder(OrderWithProducts order){
        log.info("validate order {}", order.getOrderID());
        Notification notification = new Notification();
        notification.setTitle("Place Order");
        notification.setTopic(new Topic(NotificationTopic.Order.name()));
        try {
            if(!orderService.isPendingOrder(order.getOrderID())){
                throw new InvalidOrderException("Order " + order.getOrderID() + " is not a pending order");
            }
            if(order.getEmployeeID()==null || order.getCustomerID()==null){
                throw new InvalidOrderException("Order " + order.getOrderID() + " is invalid due to missing customer or employee");
            }
            // update employeeID for order
            boolean employeeUpdateCheck = orderService.updateOrderEmployee(
                order.getOrderID(), order.getEmployeeID());
            if(employeeUpdateCheck){
                // validate and update product instock
                boolean check = orderService.validateAndProcessOrder(order);
                if(!check){
                    throw new RuntimeException("Order has been canceled");
                }
                // notification
                User user = userService.getUserByCustomerID(order.getCustomerID());
                notification.setMessage("Order #"+ order.getOrderID() +" has been confirmed");
                notification.setStatus(NotificationStatus.SUCCESSFUL.name());
                notification.setReceiverID(user.getUserID());
                // update order status
                orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Confirmed);
            } else {
                log.warn("Can not update employeeID for order " + order.getOrderID());
                throw new RuntimeException("Can not update employeeID for order "+ order.getOrderID());
            }
        } catch (RuntimeException e) {
            log.warn("Error: {}", e.getMessage());
            // notification
            User user = userService.getUserByCustomerID(order.getCustomerID());
            notification.setMessage("Order #"+order.getOrderID()+" has been canceled");
            notification.setStatus(NotificationStatus.FAILED.name());
            notification.setReceiverID(user.getUserID());
            // update order status
            orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
        } catch (InvalidDiscountException e){
            log.warn("Error: Discount is invalid {}", e.getMessage());
            // notification
            User user = userService.getUserByCustomerID(order.getCustomerID());
            notification.setMessage("Order #"+order.getOrderID()+" has been canceled due to invalid discount");
            notification.setStatus(NotificationStatus.FAILED.name());
            notification.setReceiverID(user.getUserID());
            // update order status
            orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
        } catch(InvalidOrderException e){
            log.warn("Error: Order is invalid {}", e.getMessage());
            User user = userService.getUserByCustomerID(order.getCustomerID());
            notification.setMessage("Order #"+order.getOrderID()+" has been canceled due to invalid order");
            notification.setStatus(NotificationStatus.FAILED.name());
            notification.setReceiverID(user.getUserID());
            // update order status
            orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
        }
        return notification;
    }
}
