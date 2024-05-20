package com.phucx.order.eventListener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.phucx.order.config.MessageQueueConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.ORDER_NOTIFICATION_QUEUE)
public class QueueListener {
    
    @RabbitHandler
    public void orderNotificationHandler(String notification){
        log.info("notification: {}", notification);
    }
}
