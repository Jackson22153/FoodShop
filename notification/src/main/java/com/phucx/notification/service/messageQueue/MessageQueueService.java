package com.phucx.notification.service.messageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.notification.model.DataDTO;
import com.phucx.notification.model.EventMessage;
import com.phucx.notification.model.Notification;

public interface MessageQueueService {
    // send message to message queues
    public void sendNotification(Notification notification, String exchange, String routingKey);
    // send message to user
    public void sendMessageToUser(String userID, Notification notificationMessage);
    // send message to employee's topic
    public void sendOrderNotificationToEmployeeTopic(Notification notification);
    // fetch data from other services
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange, String routingKey, Class<T> dataType) 
        throws JsonProcessingException;
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange, String routingKey, TypeReference<T> dataType) 
        throws JsonProcessingException;
    
}
