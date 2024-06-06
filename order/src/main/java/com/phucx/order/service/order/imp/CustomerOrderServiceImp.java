package com.phucx.order.service.order.imp;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.NotificationStatus;
import com.phucx.order.constant.NotificationTopic;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderItem;
import com.phucx.order.model.OrderItemDiscount;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Topic;
import com.phucx.order.service.customer.CustomerService;
import com.phucx.order.service.messageQueue.MessageQueueService;
import com.phucx.order.service.order.CustomerOrderService;
import com.phucx.order.service.order.OrderService;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerOrderServiceImp implements CustomerOrderService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private CustomerService customerService;
    
    @Override
    public OrderDetails placeOrder(OrderWithProducts order, String userID) 
        throws JsonProcessingException, InvalidDiscountException, InvalidOrderException {
        log.info("placeOrder(order={}, userID={})", order, userID);
        // fetch customer
        Customer customer = customerService.getCustomerByUserID(userID);
        order.setCustomerID(customer.getCustomerID());
        // process order
        OrderWithProducts newOrder = this.orderProcessing(order);
        // create a notification 
        if(newOrder ==null){
            throw new RuntimeException("Error when placing an order");
        }
        // create and save notification back to user
        Notification notificationToCustomer = new Notification(
            "Place Order",
            "Order #"+newOrder.getOrderID()+" has been placed successfully",
            null, userID, new Topic(NotificationTopic.Order.name()),
            NotificationStatus.SUCCESSFUL.name(), true);
        // send message back to user
        messageQueueService.sendNotification(notificationToCustomer);
        // messageQueueService.sendNotificationToUser(customer.getUserID(), notificationToUser);
        // send message to notification message queue
        Notification notificationToTopic = new Notification(
            "Place Order",
            "Order #" + newOrder.getOrderID() + " has been placed", 
            userID, null, new Topic(NotificationTopic.Order.name()), 
            NotificationStatus.SUCCESSFUL.name(), true);
        messageQueueService.sendNotification(notificationToTopic);
        // fetch saved order
        return orderService.getOrder(newOrder.getOrderID(), OrderStatus.Pending);
    }

        // order processing
    // validating and saving customer's order 
    private OrderWithProducts orderProcessing(OrderWithProducts order) throws JsonProcessingException, InvalidDiscountException, InvalidOrderException {
        log.info("orderProcessing({})", order);
        if(order.getCustomerID()==null){
            throw new NotFoundException("Customer does not found");
        }
        LocalDateTime currenDateTime = LocalDateTime.now();
        order.setOrderDate(currenDateTime);
        // set applieddate for discount;
        for (OrderItem product : order.getProducts()) {
            for(OrderItemDiscount discount : product.getDiscounts()){
                discount.setAppliedDate(currenDateTime);
            }
        }
        
        // validate order
        boolean isValidOrder = orderService.validateOrder(order);
        if(!isValidOrder) throw new InvalidOrderException("Order of customer "+order.getCustomerID()+" is not valid");
        // save order
        String pendingOrderID = orderService.saveFullOrder(order);
        order.setOrderID(pendingOrderID);
        return order;
    }

    @Override
    public void receiveOrder(OrderWithProducts order) throws JsonProcessingException {
        log.info("receiveOrder(orderID={})", order.getOrderID());
        
        OrderDetails orderDetails = orderService.getOrder(order.getOrderID(), OrderStatus.Shipping);
        Boolean status = orderService.updateOrderStatus(orderDetails.getOrderID(), OrderStatus.Successful);
        // notification
        Notification notification = new Notification();
        notification.setTitle("Receive Order");
        notification.setTopic(new Topic(NotificationTopic.Order.name()));
        log.info("status: {}", status);
        // if(status){
        //     log.info("ssnotification: {}", notification);
        //     notification.setMessage("Order #" + orderDetails.getOrderID() + " is received successully by customer " + orderDetails.getCustomerID());
        //     notification.setStatus(NotificationStatus.SUCCESSFUL.name());
        //     notification.setReceiverID(userService.getUserIdOfEmployeeID(orderDetails.getEmployeeID()));
        //     log.info("ssnotification: {}", notification);
        // }else {
        //     notification.setMessage("Order #" + orderDetails.getOrderID() + " can not received by customer " + orderDetails.getCustomerID());
        //     notification.setStatus(NotificationStatus.ERROR.name());
        //     notification.setReceiverID(userService.getUserIdOfEmployeeID(orderDetails.getEmployeeID()));
        // }
        messageQueueService.sendNotification(notification);
        // send message to employee
        // messageQueueService.sendNotification(notificationMessage);
        // send message to customer
        // notificationMessage.setReceiverID(userService.getUserIdOfEmployeeID(order.getEmployeeID()));
        // messageQueueService.sendNotification(notificationMessage);
    }
    
}
