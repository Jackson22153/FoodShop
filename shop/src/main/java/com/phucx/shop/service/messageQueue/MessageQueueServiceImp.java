package com.phucx.shop.service.messageQueue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageQueueServiceImp implements MessageQueueService {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public Object sendAndReceiveData(Object message, String queueName, String routingKey) {
        log.info("sendAndReceiveData(message={}, queueName={}, routingKey={})", message, queueName, routingKey);
        return rabbitTemplate.convertSendAndReceive(queueName, routingKey, message);
    }

    
}
