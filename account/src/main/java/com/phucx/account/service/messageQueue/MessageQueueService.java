package com.phucx.account.service.messageQueue;

import com.phucx.account.model.EventMessage;
import com.phucx.account.model.Notification;

public interface MessageQueueService {
    // send message to message queues
    public void sendNotification(Notification notification);

    public void sendEmployeeNotificationOrderToTopic(Notification notification);

    // public Notification sendAndReceiveOrder(OrderWithProducts order);

    public EventMessage<Object> sendAndReceiveData(Object message, String queueName, String routingKey);

    public void sendMessageToUser(String userID, Notification notificationMessage);

    
}
