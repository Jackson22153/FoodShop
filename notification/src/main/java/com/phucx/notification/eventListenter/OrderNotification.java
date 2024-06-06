package com.phucx.notification.eventListenter;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.notification.config.MessageQueueConfig;
import com.phucx.notification.model.Notification;
import com.phucx.notification.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.NOTIFICATION_ORDER_QUEUE)
public class OrderNotification {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageQueueService messageQueueService;

    @RabbitHandler
    private void orderNotification(String message){
        log.info("orderNotification(message={})", message);
        try {
            Notification notification = objectMapper.readValue(message, Notification.class);
            if(notification.getReceiverID()!=null){
                messageQueueService.sendMessageToUser(notification.getReceiverID(), notification);
            }else{
                messageQueueService.sendOrderNotificationToEmployeeTopic(notification);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    } 
}
