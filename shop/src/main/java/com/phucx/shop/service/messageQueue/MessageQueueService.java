package com.phucx.shop.service.messageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.shop.model.DataRequest;
import com.phucx.shop.model.EventMessage;

public interface MessageQueueService {
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataRequest> message, String queueName, String routingKey, Class<T> dataType) 
        throws JsonProcessingException;
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataRequest> message, String queueName, String routingKey, TypeReference<T> dataType) 
        throws JsonProcessingException;
}
