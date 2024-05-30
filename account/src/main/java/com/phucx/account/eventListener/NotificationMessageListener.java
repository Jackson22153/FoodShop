package com.phucx.account.eventListener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.phucx.account.config.MessageQueueConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.NOTIFICATION_QUEUE)
public class NotificationMessageListener {
    
    @RabbitHandler
    public void notificationMessageHandler(String notificationMessage){
        log.info("notificationMessageHandler: {}", notificationMessage);
    }
    
}
