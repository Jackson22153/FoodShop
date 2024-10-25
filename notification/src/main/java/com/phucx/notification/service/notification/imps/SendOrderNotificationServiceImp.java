package com.phucx.notification.service.notification.imps;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.constant.NotificationBroadCast;
import com.phucx.constant.NotificationTitle;
import com.phucx.model.OrderNotificationDTO;
import com.phucx.notification.constant.NotificationIsRead;
import com.phucx.notification.constant.WebConstant;
import com.phucx.notification.constant.WebSocketConstant;
import com.phucx.notification.exception.NotFoundException;
import com.phucx.notification.model.NotificationDetail;
import com.phucx.notification.service.messageQueue.MessageQueueService;
import com.phucx.notification.service.notification.NotificationService;
import com.phucx.notification.service.notification.SendOrderNotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SendOrderNotificationServiceImp implements SendOrderNotificationService{
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private NotificationService notificationService;
    
    @Override
    public void sendNotificationToAllEmployees(OrderNotificationDTO orderNotification) {
        log.info("sendNotificationToAllEmployees({})", orderNotification);
        NotificationDetail notificationDetail = this.convertNotification(orderNotification);
        messageQueueService.sendNotificationToTopic(notificationDetail, 
            WebSocketConstant.TOPIC_EMPLOYEE_NOTIFICAITON_ORDER);
    }

    @Override
    public void sendNotificationToAllCustomers(OrderNotificationDTO orderNotificationDTO) {
        log.info("sendNotificationToAllCustomers({})", orderNotificationDTO);
        
    }
    
    // convert from order notification to notification details
    private NotificationDetail convertNotification(OrderNotificationDTO orderNotification){
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of(WebConstant.TIME_ZONE));
        NotificationDetail notification = new NotificationDetail(
            orderNotification.getTitle().name(), orderNotification.getMessage(), 
            orderNotification.getSenderID(), orderNotification.getReceiverID(), 
            orderNotification.getPicture(),orderNotification.getTopic().name(), 
            orderNotification.getStatus().name(), NotificationIsRead.NO.getValue(), 
            currentDateTime);
        return notification;
    }

    @Override
    public void sendNotificationToEmployee(OrderNotificationDTO orderNotification) throws NotFoundException {
        log.info("sendNotificationToEmployee({})", orderNotification);
        String orderID = orderNotification.getOrderID();
        if(orderID==null) throw new RuntimeException("Order notification does not contain any orderID");
        NotificationDetail notificationDetail = this.convertNotification(orderNotification);
        log.info("notificationdetails: {}", notificationDetail);
        // get the first notification
        if(!(orderNotification.getTitle().equals(NotificationTitle.PLACE_ORDER))){
            NotificationDetail fetchedNotificationDetail = notificationService.getOrderNotificationDetail(
                NotificationTitle.PLACE_ORDER.name(), orderID, NotificationBroadCast.ALL_EMPLOYEES.name());
            // set attribute for the current notification
            notificationDetail.setPicture(fetchedNotificationDetail.getPicture());
            notificationDetail.setRepliedTo(fetchedNotificationDetail.getNotificationID());
        }

        // send notification message to a employee
        messageQueueService.sendMessageToUser(orderNotification.getReceiverID(), notificationDetail);
    }

    @Override
    public void sendNotificationToCustomer(OrderNotificationDTO orderNotification) throws NotFoundException {
        log.info("sendNotificationToCustomer({})", orderNotification);
        String orderID = orderNotification.getOrderID();
        if(orderID==null) throw new RuntimeException(
            "Order notification does not contain any orderID");
        NotificationDetail notificationDetail = 
            this.convertNotification(orderNotification);
        Boolean first = orderNotification.getFirstNotification()!=null?
            orderNotification.getFirstNotification():
            false;
        if(!first){
            // get the first notification
            if(!(orderNotification.getTitle().equals(NotificationTitle.PLACE_ORDER))){
                NotificationDetail fetchedNotification = notificationService
                    .getOrderNotificationDetail(
                        NotificationTitle.PLACE_ORDER.name(), 
                        orderID, 
                        orderNotification.getReceiverID());
                // set notification
                notificationDetail.setRepliedTo(fetchedNotification.getNotificationID());
                notificationDetail.setPicture(fetchedNotification.getPicture());
            }
            // mark as read for confirm order and cancel order to all employees
            if((orderNotification.getTitle().equals(NotificationTitle.CONFIRM_ORDER) ||
                orderNotification.getTitle().equals(NotificationTitle.CANCEL_ORDER))){
                Boolean result = notificationService
                    .updateNotificationReadStatusOfBroadcast(
                        NotificationTitle.PLACE_ORDER.name(), 
                        orderID, 
                        NotificationBroadCast.ALL_EMPLOYEES, 
                        NotificationIsRead.YES.getValue());
                if(!result) throw new RuntimeException(
                    "Notification with title " + orderNotification.getTitle() + 
                    " and OrderID " + orderID +" can not be updated to " + 
                    NotificationIsRead.YES + " status");
            }            
        }
        // send notification message to a customer
        messageQueueService.sendMessageToUser(
            orderNotification.getReceiverID(), 
            notificationDetail);
    }
    
}   
