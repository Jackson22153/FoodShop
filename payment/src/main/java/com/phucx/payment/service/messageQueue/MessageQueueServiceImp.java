package com.phucx.payment.service.messageQueue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.payment.model.DataDTO;
import com.phucx.payment.model.EventMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageQueueServiceImp implements MessageQueueService{
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> eventMessage, String exchange,
            String routingKey, Class<T> dataType) throws JsonProcessingException {
        log.info("sendAndReceiveData(eventMessage={}, exchange={}, routingKey={}, className={})", 
            eventMessage, exchange, routingKey, dataType.getName());
        String message = objectMapper.writeValueAsString(eventMessage);
        // send and receive message
        String responseMessage = (String) this.rabbitTemplate.convertSendAndReceive(exchange, routingKey, message);
        // convert message
        JavaType type = objectMapper.getTypeFactory().constructParametricType(EventMessage.class, dataType);
        EventMessage<T> response = objectMapper.readValue(responseMessage, type);
        return response;
    }
    @Override
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> eventMessage, String exchange,
            String routingKey, TypeReference<EventMessage<T>> dataType) throws JsonProcessingException {
        log.info("sendAndReceiveData(eventMessage={}, exchange={}, routingKey={}, className={})", 
            eventMessage, exchange, routingKey, dataType.toString());
        String message = objectMapper.writeValueAsString(eventMessage);
        // send and receive message
        String responseMessage = (String) this.rabbitTemplate.convertSendAndReceive(exchange, routingKey, message);
        // convert message
        EventMessage<T> response = objectMapper.readValue(responseMessage, dataType);
        return response;
    }
}
