package com.phucx.payment.service.messageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.payment.model.DataDTO;
import com.phucx.payment.model.EventMessage;

public interface MessageQueueService {
    // send and receive data from other services
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> eventMessage, String exchange, 
        String routingKey, Class<T> dataType) throws JsonProcessingException;
    // send and receive data from other services
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> eventMessage, String exchange, 
        String routingKey, TypeReference<EventMessage<T>> dataType) throws JsonProcessingException;
}
