package com.phucx.order.service.messageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.order.model.DataDTO;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderWithProducts;

public interface MessageQueueService {
    // send message to message queues
    public void sendNotification(Notification notification);
    public void sendEmployeeNotificationOrderToTopic(Notification notification);
    public Notification sendAndReceiveOrder(OrderWithProducts order);
    public void sendMessageToUser(String userID, Notification notificationMessage);
    public void sendNotificationToUser(String userID, Notification notificationMessage);

    // send and receive data from other services
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> eventMessage, String queueName, 
        String routingKey, Class<T> dataType) throws JsonProcessingException;
    // send and receive data from other services
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> eventMessage, String queueName, 
        String routingKey, TypeReference<T> dataType) throws JsonProcessingException;
}
