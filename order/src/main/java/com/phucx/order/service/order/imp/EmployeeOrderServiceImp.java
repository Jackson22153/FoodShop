package com.phucx.order.service.order.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.NotificationTopic;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.Employee;
import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Topic;
import com.phucx.order.service.employee.EmployeeService;
import com.phucx.order.service.messageQueue.MessageQueueService;
import com.phucx.order.service.order.EmployeeOrderService;
import com.phucx.order.service.order.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeOrderServiceImp implements EmployeeOrderService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private EmployeeService employeeService;

    @Override
    public void confirmOrder(OrderWithProducts order, String userID) throws InvalidOrderException, JsonProcessingException {
        log.info("confirmOrder(order={}, userID={})", order, userID);
        OrderWithProducts orderWithProducts = orderService.getPendingOrderDetail(order.getOrderID());
        // fetch employee
        Employee employee = employeeService.getEmployeeByUserID(userID);

        orderWithProducts.setEmployeeID(employee.getEmployeeID());
        // send order for server to validate it
        messageQueueService.sendOrder(orderWithProducts);
    }

    @Override
    public void cancelOrder(OrderWithProducts order, String userID) throws JsonProcessingException {
        log.info("cancelOrder(order={}, userID={})", order, userID);
        // fetch pending order
        OrderDetails orderDetail = orderService.getOrder(order.getOrderID(), OrderStatus.Pending);
        // fetch employee
        Employee employee = employeeService.getEmployeeByUserID(userID);
        order.setEmployeeID(employee.getEmployeeID());
        Boolean check = orderService.updateOrderEmployee(order.getOrderID(), employee.getUserID());
        if(!check) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated");
        // update order status as canceled
        Boolean status = orderService.updateOrderStatus(orderDetail.getOrderID(), OrderStatus.Canceled);
        if(!status) throw new RuntimeException("Order #" + order.getOrderID() + " status can not be updated to canceled status");
        // notification
        Notification notification = new Notification();
        notification.setTitle("Cancel Order");
        notification.setTopic(new Topic(NotificationTopic.Order.name()));

        // if(status){
        //     String userID = userService.getUserIdOfCustomerID(order.getCustomerID());
        //     notification.setReceiverID(userID);
        //     notification.setMessage("Order #" + order.getOrderID() + " has been canceled successfully");
        //     notification.setStatus(NotificationStatus.SUCCESSFUL.name());
        // }else {
        //     String userID = userService.getUserIdOfEmployeeID(employeeID);
        //     notification.setReceiverID(userID);
        //     notification.setMessage("Order #" + order.getOrderID() + " can not be canceled");
        //     notification.setStatus(NotificationStatus.ERROR.name());
        // }

        messageQueueService.sendNotification(notification);
    }

    @Override
    public void fulfillOrder(OrderWithProducts order) throws JsonProcessingException {
        log.info("fullfillOrder({})", order);
        OrderDetails fetchedOrder = orderService.getOrder(order.getOrderID(), OrderStatus.Confirmed);
        Boolean status = orderService.updateOrderStatus(fetchedOrder.getOrderID(), OrderStatus.Shipping);
        if(!status) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated to shipping status");
        // notification
        Notification notification = new Notification();
        notification.setTitle("Fulfill Order");
        notification.setTopic(new Topic(NotificationTopic.Order.name()));
        // if(status){
        //     String userID = userService.getUserIdOfCustomerID(order.getCustomerID());
        //     notification.setReceiverID(userID);
        //     notification.setMessage("Order #" + order.getOrderID() + " has been fulfilled");
        //     notification.setStatus(NotificationStatus.SUCCESSFUL.name());
        // }else {
        //     String userID = userService.getUserIdOfEmployeeID(order.getEmployeeID());
        //     notification.setReceiverID(userID);
        //     notification.setMessage("Order #" + order.getOrderID() + " can not be fulfilled");
        //     notification.setStatus(NotificationStatus.ERROR.name());
        // }
        messageQueueService.sendNotification(notification);
    }
    
}
