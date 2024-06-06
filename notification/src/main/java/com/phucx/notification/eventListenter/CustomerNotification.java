package com.phucx.notification.eventListenter;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.phucx.notification.config.MessageQueueConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.NOTIFICATION_CUSTOMER_QUEUE)
public class CustomerNotification {
    
    @RabbitHandler
    private void notificationListener(String message){
        log.info("notificationListener(message={})", message);
    }
}
