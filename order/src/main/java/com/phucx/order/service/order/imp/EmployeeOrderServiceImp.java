package com.phucx.order.service.order.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.NotificationStatus;
import com.phucx.order.constant.NotificationTopic;
import com.phucx.order.constant.OrderNotificationTitle;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.Employee;
import com.phucx.order.model.NotificationDetail;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.User;
import com.phucx.order.service.employee.EmployeeService;
import com.phucx.order.service.messageQueue.MessageQueueService;
import com.phucx.order.service.order.EmployeeOrderService;
import com.phucx.order.service.order.OrderService;
import com.phucx.order.service.user.UserService;

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
    @Autowired
    private UserService userService;

    @Override
    public void confirmOrder(String orderID, String userID) throws InvalidOrderException, JsonProcessingException {
        log.info("confirmOrder(orderID={}, userID={})", orderID, userID);
        OrderWithProducts orderWithProducts = orderService.getPendingOrderDetail(orderID);
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
        Boolean check = orderService.updateOrderEmployee(order.getOrderID(), employee.getEmployeeID());
        if(!check) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated");
        // update order status as canceled
        Boolean status = orderService.updateOrderStatus(orderDetail.getOrderID(), OrderStatus.Canceled);
        if(!status) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated to canceled status");
        // notification
        NotificationDetail notification = new NotificationDetail();
        notification.setTitle(OrderNotificationTitle.CANCEL_ORDER.getValue());
        notification.setTopic(NotificationTopic.Order.name());

        if(status){
            // send message to customer
            User fetchedUser = userService.getUserByCustomerID(order.getCustomerID());
            notification.setReceiverID(fetchedUser.getUserID());
            notification.setMessage("Order #" + order.getOrderID() + " has been canceled");
            notification.setStatus(NotificationStatus.SUCCESSFUL.name());
        }else {
            // send message to employee
            notification.setReceiverID(userID);
            notification.setMessage("Error: Order #" + order.getOrderID() + " can not be canceled");
            notification.setStatus(NotificationStatus.ERROR.name());
        }

        messageQueueService.sendNotification(notification);
    }

    @Override
    public void fulfillOrder(OrderWithProducts order, String userID) throws JsonProcessingException {
        log.info("fullfillOrder(order={}, userID={})", order, userID);
        OrderDetails fetchedOrder = orderService.getOrder(order.getOrderID(), OrderStatus.Confirmed);
        Boolean status = orderService.updateOrderStatus(fetchedOrder.getOrderID(), OrderStatus.Shipping);
        if(!status) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated to shipping status");
        // notification
        NotificationDetail notification = new NotificationDetail();
        notification.setTitle(OrderNotificationTitle.FULFILL_ORDER.getValue());
        notification.setTopic(NotificationTopic.Order.name());
        if(status){
            // send message to customer
            User fetchedUser = userService.getUserByCustomerID(order.getCustomerID());
            notification.setReceiverID(fetchedUser.getUserID());
            notification.setMessage("Order #" + order.getOrderID() + " has been fulfilled");
            notification.setStatus(NotificationStatus.SUCCESSFUL.name());
        }else {
            // send message to employee
            notification.setReceiverID(userID);
            notification.setMessage("Error: Order #" + order.getOrderID() + " can not be fulfilled");
            notification.setStatus(NotificationStatus.ERROR.name());
        }
        messageQueueService.sendNotification(notification);
    }

    @Override
    public Page<OrderDetails> getOrders(String userID, OrderStatus status, int pageNumber, int pageSize)
            throws JsonProcessingException {
        log.info("getOrders(userID={}, status={}, pageNumber={}, pageSize={})", userID, status, pageNumber, pageSize);
        Employee fetchedEmployee = employeeService.getEmployeeByUserID(userID);
        Page<OrderDetails> orders = null;
        if(status.equals(OrderStatus.All)){
            orders = orderService.getOrdersByEmployeeID(
                fetchedEmployee.getEmployeeID(), 
                pageNumber, pageSize);
        }else if(status.equals(OrderStatus.Pending)){
            orders = orderService.getOrders(OrderStatus.Pending, pageNumber, pageSize);
        }else{
            orders = orderService.getOrdersByEmployeeID(
                fetchedEmployee.getEmployeeID(), 
                status, pageNumber, pageSize);
        }
        return orders;
    }

    @Override
    public OrderWithProducts getOrder(String orderID, String userID, OrderStatus status) throws JsonProcessingException {
        log.info("getOrder(orderID={}, userID={}, orderStatus={})", orderID, userID, status);
        Employee fetchedEmployee = employeeService.getEmployeeByUserID(userID);
        OrderWithProducts order = null;
        if(status.equals(OrderStatus.Pending)){
            order = orderService.getPendingOrderDetail(orderID);
        }else if(status.equals(OrderStatus.All)){
            order = orderService.getOrderByEmployeeID(fetchedEmployee.getEmployeeID(), orderID);
        }else {
            order = orderService.getOrderByEmployeeID(fetchedEmployee.getEmployeeID(), orderID, status);
        }
        return order;
    }
}