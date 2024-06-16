package com.phucx.notification.eventListenter;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.notification.config.MessageQueueConfig;
import com.phucx.notification.constant.EventType;
import com.phucx.notification.constant.NotificationBroadCast;
import com.phucx.notification.constant.NotificationIsRead;
import com.phucx.notification.constant.WebConstant;
import com.phucx.notification.constant.WebSocketConstant;
import com.phucx.notification.model.EventMessage;
import com.phucx.notification.model.NotificationDetail;
import com.phucx.notification.model.OrderNotificationDTO;
import com.phucx.notification.service.messageQueue.MessageQueueService;
import com.phucx.notification.service.notification.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.NOTIFICATION_ORDER_QUEUE)
public class OrderNotification {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private NotificationService notificationService;

    @RabbitHandler
    private void orderNotification(String message){
        log.info("orderNotification(message={})", message);
        try {
            // extract notification from message
            TypeReference<EventMessage<OrderNotificationDTO>> typeReference = new TypeReference<EventMessage<OrderNotificationDTO>>() {};
            EventMessage<OrderNotificationDTO> eventMessage = objectMapper.readValue(message, typeReference);
            OrderNotificationDTO payload = eventMessage.getPayload();
            // process message
            if(eventMessage.getEventType().equals(EventType.SendOrderNotificationToUser)){
                // send message to user
                NotificationDetail notification = convertNotification(payload);
                if(payload.getReceiverID().equalsIgnoreCase(NotificationBroadCast.ALL_EMPLOYEES.name())){
                    messageQueueService.sendNotificationToTopic(notification, WebSocketConstant.TOPIC_EMPLOYEE_NOTIFICAITON_ORDER);
                }else if(payload.getReceiverID().equalsIgnoreCase(NotificationBroadCast.ALL_CUSTOMERS.name())){

                }else {
                    messageQueueService.sendMessageToUser(notification.getReceiverID(), notification);
                }
            }else if(eventMessage.getEventType().equals(EventType.MarkOrderAsConfirmed)){
                // mark pending order notification as read after confirming
                Boolean updatedStatus = notificationService.updateNotificationReadStatusOfBroadcast(
                    payload.getTitle().name(), payload.getOrderID(), NotificationBroadCast.ALL_EMPLOYEES, 
                    NotificationIsRead.YES.getValue());
                if(!updatedStatus) throw new RuntimeException("Error while updating notification "+ payload.toString() + " read status");  
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    } 

    private NotificationDetail convertNotification(OrderNotificationDTO orderNotification){
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of(WebConstant.TIME_ZONE));
        NotificationDetail notification = new NotificationDetail(
            orderNotification.getTitle().name(), orderNotification.getMessage(), 
            orderNotification.getSenderID(), orderNotification.getReceiverID(), 
            orderNotification.getTopic().name(), orderNotification.getStatus().name(), 
            NotificationIsRead.NO.getValue(), currentDateTime);
        return notification;
    }
}
