package com.phucx.account.service.messageQueue.sender;

import com.phucx.account.model.Notification;
import com.phucx.account.model.OrderWithProducts;

public interface MessageSender {
    // send message to message queues
    public void sendNotification(Notification notification);

    public void sendEmployeeNotificationOrderToTopic(Notification notification);

    // public Notification sendAndReceiveOrder(OrderWithProducts order);

    public void sendMessageToUser(String userID, Notification notificationMessage);
}
