package com.phucx.account.service.messageQueue.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.account.model.UserOrderProducts;

@Service
public class MessageSenderImp implements MessageSender{
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void send(String exchange, String routingKey, UserOrderProducts userOrderProducts) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(userOrderProducts);
        this.rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
    
}
