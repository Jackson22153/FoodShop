package com.phucx.notification.service.notification.imps;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.notification.constant.NotificationIsRead;
import com.phucx.notification.constant.WebConstant;
import com.phucx.notification.constant.WebSocketConstant;
import com.phucx.notification.model.NotificationDetail;
import com.phucx.notification.model.UserNotificationDTO;
import com.phucx.notification.service.messageQueue.MessageQueueService;
import com.phucx.notification.service.notification.SendUserNotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SendUserNotificationServiceImp implements SendUserNotificationService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public void sendNotificationToCustomer(UserNotificationDTO userNotificationDTO) {
        log.info("sendNotificationToEmployee({})", userNotificationDTO);
        NotificationDetail notification = convertNotification(userNotificationDTO);
        // send notification to a specific customer
        messageQueueService.sendMessageToUser(notification.getReceiverID(), notification);
    }

    @Override
    public void sendNotificationToEmployee(UserNotificationDTO userNotificationDTO) {
        log.info("sendNotificationToEmployee({})", userNotificationDTO);
        NotificationDetail notification = convertNotification(userNotificationDTO);
        // send notification to a specific employee
        messageQueueService.sendMessageToUser(notification.getReceiverID(), notification);
    }

    // converter
    private NotificationDetail convertNotification(UserNotificationDTO userNotification){
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of(WebConstant.TIME_ZONE));
        NotificationDetail notification = new NotificationDetail(
            userNotification.getTitle().name(), userNotification.getMessage(), 
            userNotification.getSenderID(), userNotification.getReceiverID(), 
            userNotification.getPicture(),userNotification.getTopic().name(), 
            userNotification.getStatus().name(), NotificationIsRead.NO.getValue(), 
            currentDateTime);
        return notification;
    }

    @Override
    public void sendNotificationToAllEmployees(UserNotificationDTO userNotificationDTO) {
        log.info("sendNotificationToAllEmployees({})", userNotificationDTO);
        NotificationDetail notification = convertNotification(userNotificationDTO);
        messageQueueService.sendNotificationToTopic(notification, 
            WebSocketConstant.TOPIC_EMPLOYEE_NOTIFICAITON_ACCOUNT);
    }

    @Override
    public void sendNotificationToAllCustomers(UserNotificationDTO userNotificationDTO) {
        log.info("sendNotificationToAllCustomers({})", userNotificationDTO);
        NotificationDetail notification = convertNotification(userNotificationDTO);
        messageQueueService.sendNotificationToTopic(notification, 
            WebSocketConstant.TOPIC_CUSTOMER_NOTIFICAITON_ACCOUNT);
    }
    
}
