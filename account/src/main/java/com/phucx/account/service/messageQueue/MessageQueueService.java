package com.phucx.account.service.messageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.account.model.DataRequest;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.Notification;

public interface MessageQueueService {
    // send message to message queues
    public void sendNotification(Notification notification);

    public void sendEmployeeNotificationOrderToTopic(Notification notification);

    // public Notification sendAndReceiveOrder(OrderWithProducts order);

    public EventMessage<Object> sendAndReceiveData(Object message, String queueName, String routingKey);

    public void sendMessageToUser(String userID, Notification notificationMessage);
    // fetch data from other services
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataRequest> message, String queueName, String routingKey, Class<T> dataType) 
        throws JsonProcessingException;
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataRequest> message, String queueName, String routingKey, TypeReference<T> dataType) 
        throws JsonProcessingException;
    
}
