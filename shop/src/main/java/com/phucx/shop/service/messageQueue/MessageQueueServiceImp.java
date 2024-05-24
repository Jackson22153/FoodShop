package com.phucx.shop.service.messageQueue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.EventMessage;

@Service
public class MessageQueueServiceImp implements MessageQueueService {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public Object sendAndReceiveData(EventMessage message) {
        return rabbitTemplate.convertSendAndReceive(message);
    }

    
}
