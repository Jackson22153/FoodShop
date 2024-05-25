package com.phucx.order.service.employee;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.phucx.order.constant.EventType;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.constant.NotificationTopic;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.Employee;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderDetailDTO;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Topic;
import com.phucx.order.model.UserRequest;
import com.phucx.order.service.messageQueue.MessageQueueService;
import com.phucx.order.service.notification.NotificationService;
import com.phucx.order.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImp implements EmployeeService {
    @Autowired
    private NotificationService notificationService;
    // @Autowired
    // private OrderService orderService;
    @Autowired
    private MessageQueueService messageQueueService;

    // PROCESSING ORDER
    // @Override
    // public Notification confirmOrder(OrderWithProducts order, String employeeID) throws InvalidOrderException {
    //     log.info("confirmOrder(order={}, employeeID={})", order, employeeID);
    //     OrderWithProducts orderWithProducts = orderService.getPendingOrderDetail(order.getOrderID());
    //     orderWithProducts.setEmployeeID(employeeID);
    //     // send order for server to validate it
    //     Notification response = messageQueueService.sendAndReceiveOrder(orderWithProducts);
    //     return response;
    // }

    // @Override
    // public Notification cancelOrder(OrderWithProducts order, String employeeID) {
    //     log.info("cancelOrder(order={}, employeeID={})", order, employeeID);
    //     // fetch pending order
    //     OrderDetailDTO orderDetail = orderService.getOrder(
    //         order.getOrderID(), OrderStatus.Pending);
    //     Boolean check = orderService.updateOrderEmployee(order.getOrderID(), employeeID);
    //     if(!check) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated");
    //     // update order status as canceled
    //     // Boolean status = orderService.updateOrderStatus(orderDetail.getOrderID(), OrderStatus.Canceled);
    //     // notification
    //     Notification notification = new Notification();
    //     notification.setTitle("Cancel Order");
    //     notification.setTopic(new Topic(NotificationTopic.Order.name()));
    //     // if(status){
    //     //     String userID = userService.getUserIdOfCustomerID(order.getCustomerID());
    //     //     notification.setReceiverID(userID);
    //     //     notification.setMessage("Order #" + order.getOrderID() + " has been canceled successfully");
    //     //     notification.setStatus(NotificationStatus.SUCCESSFUL.name());
    //     // }else {
    //     //     String userID = userService.getUserIdOfEmployeeID(employeeID);
    //     //     notification.setReceiverID(userID);
    //     //     notification.setMessage("Order #" + order.getOrderID() + " can not be canceled");
    //     //     notification.setStatus(NotificationStatus.ERROR.name());
    //     // }
    //     return notification;
    // }

    // @Override
    // public Notification fulfillOrder(OrderWithProducts order) {
    //     log.info("fulfillOrder(orderID={})", order.getOrderID());
    //     OrderDetailDTO fetchedOrder = orderService.getOrder(order.getOrderID(), OrderStatus.Confirmed);
    //     Boolean status = orderService.updateOrderStatus(fetchedOrder.getOrderID(), OrderStatus.Shipping);
    //     // notification
    //     Notification notification = new Notification();
    //     notification.setTitle("Fulfill Order");
    //     notification.setTopic(new Topic(NotificationTopic.Order.name()));
    //     // if(status){
    //     //     String userID = userService.getUserIdOfCustomerID(order.getCustomerID());
    //     //     notification.setReceiverID(userID);
    //     //     notification.setMessage("Order #" + order.getOrderID() + " has been fulfilled");
    //     //     notification.setStatus(NotificationStatus.SUCCESSFUL.name());
    //     // }else {
    //     //     String userID = userService.getUserIdOfEmployeeID(order.getEmployeeID());
    //     //     notification.setReceiverID(userID);
    //     //     notification.setMessage("Order #" + order.getOrderID() + " can not be fulfilled");
    //     //     notification.setStatus(NotificationStatus.ERROR.name());
    //     // }
    //     return notification;
    // }

    @Override
    public Page<Notification> getNotifications(String userID, int pageNumber, int pageSize) {
        return notificationService.getNotificationsByReceiverIDOrNull(userID, pageNumber, pageSize);
    }

    @Override
    public Boolean turnOffNotification(String notificationID, String userID) {
        Notification notification = notificationService
            .getNotificationByUserIDOrNullAndNotificationID(userID, notificationID);
        return notificationService.updateNotificationActive(
            notification.getNotificationID(), false);
    }

    @Override
    public Employee getEmployeeByID(String employeeID) {
        log.info("getEmployeeByID(employeeID={})", employeeID);
        // create a request for user
        UserRequest userRequest = new UserRequest();
        userRequest.setEmployeeID(employeeID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<UserRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetEmployeeByID);
        eventMessage.setPayload(userRequest);
        // receive data
        EventMessage<Object> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_QUEUE, 
            MessageQueueConstant.ACCOUNT_ROUTING_KEY);
        log.info("response={}", response);
        return  (Employee) response.getPayload();
    }
}
