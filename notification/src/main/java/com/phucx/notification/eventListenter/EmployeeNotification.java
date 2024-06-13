package com.phucx.notification.eventListenter;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.notification.config.MessageQueueConfig;
import com.phucx.notification.constant.NotificationBroadCast;
import com.phucx.notification.constant.WebSocketConstant;
import com.phucx.notification.model.NotificationDetail;
import com.phucx.notification.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.NOTIFICATION_EMPLOYEE_QUEUE)
public class EmployeeNotification {
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitHandler
    private void notificationListener(String message){
        log.info("notificationListener(message={})", message);
        try {
            NotificationDetail notification = objectMapper.readValue(message, NotificationDetail.class);
            if(notification.getReceiverID().equalsIgnoreCase(NotificationBroadCast.ALL_EMPLOYEES.name())){
                // notification is send to all employees
                messageQueueService.sendNotificationToTopic(notification, WebSocketConstant.TOPIC_EMPLOYEE_NOTIFICAITON_ACCOUNT);
            }else {
                // send notification to a specific employee
                messageQueueService.sendMessageToUser(notification.getReceiverID(), notification);
            }
        } catch (Exception e) {
           log.error("Error: {}", e.getMessage());
        }
    }
}