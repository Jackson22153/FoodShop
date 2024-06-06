package com.phucx.account.service.messageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.account.model.DataDTO;
import com.phucx.account.model.EventMessage;

public interface MessageQueueService {
    // fetch data from other services
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange, String routingKey, Class<T> dataType) 
        throws JsonProcessingException;
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange, String routingKey, TypeReference<T> dataType) 
        throws JsonProcessingException;
    
}
