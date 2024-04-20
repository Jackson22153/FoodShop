package com.phucx.account.eventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.service.order.OrderService;
import com.phucx.account.annotations.LoggerAspect;
import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.constant.Notification;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;

@Component
@RabbitListener(queues = MessageQueueConfig.ORDER_QUEUE)
public class OrdersListener {
    private Logger logger = LoggerFactory.getLogger(OrdersListener.class);
    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public NotificationMessage receiver(OrderWithProducts order){
        return validateOrder(order);
    }
    
    @LoggerAspect
    // validate order product's stocks
    private NotificationMessage validateOrder(OrderWithProducts order){
        logger.info("validate order {}", order.getOrderID());
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setContent("Your order is not valid");
        try {
            if(orderService.isPendingOrder(order.getOrderID())){
                if(order.getEmployeeID()!=null && order.getCustomerID()!=null){
                    boolean employeeUpdateCheck = orderService.updateOrderEmployee(order.getOrderID(), order.getEmployeeID());
                    if(employeeUpdateCheck){
                        boolean check = orderService.validateAndProcessOrder(order);
                        if(check){
                            notificationMessage.setContent("Your order has been placed successfully");
                            notificationMessage.setStatus(Notification.SUCCESS);
                            orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Successful);
                        }else throw new RuntimeException("Order has been cancled");
                    } else {
                        logger.info("Can not update employeeID");
                        throw new RuntimeException("Can not update employeeID");
                    }
                }
            }
        } catch (RuntimeException e) {
            logger.info("Error: {}", e.getMessage());
            notificationMessage.setContent("Your order has been canceled");
            notificationMessage.setStatus(Notification.FAIL);
            orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
        } catch (InvalidDiscountException e){
            logger.info("Error: Discount is invalid {}", e.getMessage());
            notificationMessage.setContent("Your order has been canceled");
            notificationMessage.setStatus(Notification.FAIL);
            orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
        } catch(InvalidOrderException e){
            logger.info("Error: Order is invalid {}", e.getMessage());
            notificationMessage.setContent("Your order has been canceled");
            notificationMessage.setStatus(Notification.FAIL);
            orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
        }
        return notificationMessage;
    }

}
