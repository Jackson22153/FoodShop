package com.phucx.account.service.messageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.account.model.DataDTO;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.Notification;

public interface MessageQueueService {
    // send message to message queues
    public void sendNotification(Notification notification);

    public void sendEmployeeNotificationOrderToTopic(Notification notification);

    // public Notification sendAndReceiveOrder(OrderWithProducts order);

    public void sendMessageToUser(String userID, Notification notificationMessage);
    // fetch data from other services
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange, String routingKey, Class<T> dataType) 
        throws JsonProcessingException;
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange, String routingKey, TypeReference<T> dataType) 
        throws JsonProcessingException;
    
}
