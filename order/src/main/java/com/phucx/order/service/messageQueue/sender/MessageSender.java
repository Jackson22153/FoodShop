package com.phucx.order.service.messageQueue.sender;

import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderWithProducts;

public interface MessageSender {
    // send message to message queues
    public void sendNotification(Notification notification);

    public void sendEmployeeNotificationOrderToTopic(Notification notification);

    public Notification sendAndReceiveOrder(OrderWithProducts order);
    public void sendMessageToUser(String userID, Notification notificationMessage);
    public void sendNotificationToUser(String userID, Notification notificationMessage);
}
