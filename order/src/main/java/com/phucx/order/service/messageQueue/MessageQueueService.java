package com.phucx.order.service.messageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.order.model.DataDTO;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.NotificationDetail;
import com.phucx.order.model.OrderWithProducts;

public interface MessageQueueService {
    // send notification
    public void sendNotification(NotificationDetail notification) throws JsonProcessingException;
    // send order to message queue
    public void sendOrder(OrderWithProducts order) throws JsonProcessingException;
    // send and receive data from other services
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> eventMessage, String exchange, 
        String routingKey, Class<T> dataType) throws JsonProcessingException;
    // send and receive data from other services
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> eventMessage, String exchange, 
        String routingKey, TypeReference<EventMessage<T>> dataType) throws JsonProcessingException;
}
