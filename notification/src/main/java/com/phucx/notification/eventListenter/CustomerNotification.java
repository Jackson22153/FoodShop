package com.phucx.notification.eventListenter;


import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.notification.config.MessageQueueConfig;
import com.phucx.notification.constant.EventType;
import com.phucx.notification.constant.NotificationBroadCast;
import com.phucx.notification.constant.NotificationIsRead;
import com.phucx.notification.constant.WebConstant;
import com.phucx.notification.constant.WebSocketConstant;
import com.phucx.notification.model.EventMessage;
import com.phucx.notification.model.NotificationDetail;
import com.phucx.notification.model.UserNotificationDTO;
import com.phucx.notification.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.NOTIFICATION_CUSTOMER_QUEUE)
public class CustomerNotification {
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitHandler
    private void customerNotificationListener(String message){
        log.info("customerNotificationListener(message={})", message);
        try {
            TypeReference<EventMessage<UserNotificationDTO>> typeReference = new TypeReference<EventMessage<UserNotificationDTO>>() {};
            EventMessage<UserNotificationDTO> eventMessage = objectMapper.readValue(message, typeReference);
            // process message
            if(eventMessage.getEventType().equals(EventType.SendCustomerNotificationToUser)){
                // send message to a user
                UserNotificationDTO payload = eventMessage.getPayload();
                NotificationDetail notification = convertNotification(payload);
                if(notification.getReceiverID().equalsIgnoreCase(NotificationBroadCast.ALL_CUSTOMERS.name())){
                    // notification is send to all customers
                    messageQueueService.sendNotificationToTopic(notification, WebSocketConstant.TOPIC_CUSTOMER_NOTIFICAITON_ACCOUNT);
                }else {
                    // send notification to a specific customer
                    messageQueueService.sendMessageToUser(notification.getReceiverID(), notification);
                }
            }

        } catch (Exception e) {
           log.error("Error: {}", e.getMessage());
        }
    }

    // converter
    private NotificationDetail convertNotification(UserNotificationDTO userNotification){
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of(WebConstant.TIME_ZONE));
        NotificationDetail notification = new NotificationDetail(
            userNotification.getTitle().name(), userNotification.getMessage(), 
            userNotification.getSenderID(), userNotification.getReceiverID(), 
            userNotification.getTopic().name(), userNotification.getStatus().name(), 
            NotificationIsRead.NO.getValue(), currentDateTime);
        return notification;
    }
}
