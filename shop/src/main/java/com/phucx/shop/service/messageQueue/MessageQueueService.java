package com.phucx.shop.service.messageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.shop.model.DataDTO;
import com.phucx.shop.model.EventMessage;

public interface MessageQueueService {
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange, String routingKey, Class<T> dataType) 
        throws JsonProcessingException;
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange, String routingKey, TypeReference<T> dataType) 
        throws JsonProcessingException;
}
