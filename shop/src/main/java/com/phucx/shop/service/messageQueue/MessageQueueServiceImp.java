package com.phucx.shop.service.messageQueue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.shop.model.DataDTO;
import com.phucx.shop.model.EventMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageQueueServiceImp implements MessageQueueService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange, String routingKey,
            Class<T> dataType) throws JsonProcessingException {
        log.info("sendAndReceiveData(message={}, exchange={}, routingKey={}, dataType={})", message, exchange, routingKey, dataType);
        String messageJson = objectMapper.writeValueAsString(message);
        String response = (String) rabbitTemplate.convertSendAndReceive(exchange, routingKey, messageJson);
        JavaType type = objectMapper.getTypeFactory().constructParametricType(EventMessage.class, dataType);
        EventMessage<T> responseMessage = objectMapper.readValue(response, type);
        return responseMessage;
    }

    @Override
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange, String routingKey,
            TypeReference<T> dataType) throws JsonProcessingException {
        log.info("sendAndReceiveData(message={}, exchange={}, routingKey={}, dataType={})", message, exchange, routingKey, dataType.getType());
        String messageJson = objectMapper.writeValueAsString(message);
        String response = (String) rabbitTemplate.convertSendAndReceive(exchange, routingKey, messageJson);
        TypeReference<EventMessage<T>> typeRef = new TypeReference<EventMessage<T>>() {};
        EventMessage<T>  responseMessage = objectMapper.readValue(response, typeRef);
        return responseMessage;
    }

    
}
