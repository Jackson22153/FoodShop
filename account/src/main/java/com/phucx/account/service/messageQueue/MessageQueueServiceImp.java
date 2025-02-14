package com.phucx.account.service.messageQueue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.model.DataDTO;
import com.phucx.model.EventMessage;
import com.phucx.model.NotificationDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageQueueServiceImp implements MessageQueueService{
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange,
            String routingKey, Class<T> dataType) throws JsonProcessingException {
        log.info("sendAndReceiveData(message={}, exchange={}, routingKey={}, dataType={})", 
            message, exchange, routingKey, dataType.getName());
        String messageJson = objectMapper.writeValueAsString(message);
        String response = (String) rabbitTemplate.convertSendAndReceive(exchange, routingKey, messageJson);
        // convert response message
        JavaType type = objectMapper.getTypeFactory().constructParametricType(EventMessage.class, dataType);
        EventMessage<T>  responseMessage = objectMapper.readValue(response, type);
        return responseMessage;
    }
    @Override
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange,
            String routingKey, TypeReference<EventMessage<T>> dataType) throws JsonProcessingException {
        log.info("sendAndReceiveData(message={}, exchange={}, routingKey={}, className={})", 
            message, exchange, routingKey, dataType.toString());
        String messageJson = objectMapper.writeValueAsString(message);
        // send and receive message
        String responseMessage = (String) this.rabbitTemplate.convertSendAndReceive(exchange, routingKey, messageJson);
        // convert message
        EventMessage<T> response = objectMapper.readValue(responseMessage, dataType);
        return response;
    }

    @Override
    public void sendNotification(EventMessage<NotificationDTO> notification, String exchange, String routingKey) throws JsonProcessingException{
        log.info("sendNotification(notification={}, exchange={}, routingKey={})", notification, exchange, routingKey);
        String message = objectMapper.writeValueAsString(notification);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
