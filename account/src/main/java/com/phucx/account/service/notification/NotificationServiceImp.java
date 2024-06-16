package com.phucx.account.service.notification;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.account.constant.EventType;
import com.phucx.account.constant.MessageQueueConstant;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.NotificationDTO;
import com.phucx.account.model.UserNotificationDTO;
import com.phucx.account.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationServiceImp implements NotificationService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public void sendCustomerNotification(UserNotificationDTO userNotification) throws JsonProcessingException {
        log.info("sendCustomerNotification({})", userNotification);
        String eventID = UUID.randomUUID().toString();
        EventMessage<NotificationDTO> eventMessage = new EventMessage<NotificationDTO>(
            eventID, EventType.SendCustomerNotificationToUser, userNotification);
        messageQueueService.sendNotification(eventMessage, 
            MessageQueueConstant.NOTIFICATION_EXCHANGE, 
            MessageQueueConstant.CUSTOMER_NOTIFICATION_ROUTING_KEY);
    }

    @Override
    public void sendEmployeeNotification(UserNotificationDTO userNotification) throws JsonProcessingException {
        log.info("sendEmployeeNotification({})", userNotification);
        String eventID = UUID.randomUUID().toString();
        EventMessage<NotificationDTO> eventMessage = new EventMessage<NotificationDTO>(
            eventID, EventType.SendEmployeeNotificationToUser, userNotification);
        messageQueueService.sendNotification(eventMessage, 
            MessageQueueConstant.NOTIFICATION_EXCHANGE, 
            MessageQueueConstant.EMPLOYEE_NOTIFICATION_ROUTING_KEY);
    }

    
}
