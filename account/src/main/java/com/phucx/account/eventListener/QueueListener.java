package com.phucx.account.eventListener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.constant.EventType;
import com.phucx.account.model.Customer;
import com.phucx.account.model.EventMessage;
import com.phucx.account.service.customer.CustomerService;

@Slf4j
@Component
@RabbitListeners({
    // @RabbitListener(queues = MessageQueueConfig.ORDER_QUEUE),
    @RabbitListener(queues = MessageQueueConfig.ACCOUNT_QUEUE),
    @RabbitListener(queues = MessageQueueConfig.NOTIFICATION_QUEUE)
})
public class QueueListener {
    // @Autowired
    // private OrderService orderService;
    // @Autowired
    // private UserService userService;
    // @Autowired
    // private MessageQueueService messageQueueService;
    @Autowired
    private CustomerService customerService;

    @RabbitHandler
    public EventMessage<Customer> getCustomerData(EventMessage<Customer> requestCustomer){
        log.info("getCustomerData({})", requestCustomer);
        Customer customer = requestCustomer.getPayload();
        if(requestCustomer.getEventType().equals(EventType.GetCustomerByCustomerID)){
            Customer fetchedCustomer = customerService.getCustomerByID(customer.getCustomerID());
            requestCustomer.setPayload(fetchedCustomer);
        }else if(requestCustomer.getEventType().equals(EventType.GetCustomerByUserID)){
            Customer fetchedCustomer = customerService.getCustomerByUserID(customer.getUserID());
            requestCustomer.setPayload(fetchedCustomer);
        }

        return requestCustomer;

    }


    

    // @LoggerAspect
    // @RabbitHandler
    // public void notificationOrderReceiver(Notification notification){
    //     log.info("notificationReceiver: {}", notification);
    //     if(notification.getReceiverID()!=null){
    //         messageSender.sendMessageToUser(notification.getReceiverID(), notification);
    //     }else{
    //         messageSender.sendEmployeeNotificationOrderToTopic(notification);
    //     }
    // }

    // @RabbitHandler
    // public Notification orderReceiver(OrderWithProducts order){
    //     return validateOrder(order);
    // }
    
    // @LoggerAspect
    // // validate order product's stocks
    // private Notification validateOrder(OrderWithProducts order){
    //     log.info("validate order {}", order.getOrderID());
    //     Notification notification = new Notification();
    //     notification.setTitle("Place Order");
    //     notification.setTopic(new Topic(NotificationTopic.Order.name()));
    //     try {
    //         if(orderService.isPendingOrder(order.getOrderID())){
    //             if(order.getEmployeeID()!=null && order.getCustomerID()!=null){
    //                 boolean employeeUpdateCheck = orderService.updateOrderEmployee(order.getOrderID(), order.getEmployeeID());
    //                 if(employeeUpdateCheck){
    //                     boolean check = orderService.validateAndProcessOrder(order);
    //                     if(check){
    //                         // notification
    //                         String userID = userService.getUserIdOfCustomerID(order.getCustomerID());
    //                         notification.setMessage("Order #"+ order.getOrderID() +" has been confirmed");
    //                         notification.setStatus(NotificationStatus.SUCCESSFUL.name());
    //                         notification.setReceiverID(userID);
    //                         // update order status
    //                         orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Confirmed);
    //                     }else throw new RuntimeException("Order has been canceled");
    //                 } else {
    //                     log.info("Can not update employeeID");
    //                     throw new RuntimeException("Can not update employeeID");
    //                 }
    //             }
    //         }
    //     } catch (RuntimeException e) {
    //         log.warn("Error: {}", e.getMessage());
    //         // notification
    //         String userID = userService.getUserIdOfCustomerID(order.getCustomerID());
    //         notification.setMessage("Order #"+order.getOrderID()+" has been canceled");
    //         notification.setStatus(NotificationStatus.FAILED.name());
    //         notification.setReceiverID(userID);
    //         // update order status
    //         orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
    //     } catch (InvalidDiscountException e){
    //         log.warn("Error: Discount is invalid {}", e.getMessage());
    //         // notification
    //         String userID = userService.getUserIdOfCustomerID(order.getCustomerID());
    //         notification.setMessage("Order #"+order.getOrderID()+" has been canceled due to invalid discount");
    //         notification.setStatus(NotificationStatus.FAILED.name());
    //         notification.setReceiverID(userID);
    //         // update order status
    //         orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
    //     } catch(InvalidOrderException e){
    //         log.warn("Error: Order is invalid {}", e.getMessage());
    //         String userID = userService.getUserIdOfCustomerID(order.getCustomerID());
    //         notification.setMessage("Order #"+order.getOrderID()+" has been canceled due to invalid order");
    //         notification.setStatus(NotificationStatus.FAILED.name());
    //         notification.setReceiverID(userID);
    //         // update order status
    //         orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
    //     }
    //     return notification;
    // }

}
