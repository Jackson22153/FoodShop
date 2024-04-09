package com.phucx.account.service.messageQueue.sender;

import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderWithProducts;

public interface MessageSender {
    // public void send(String exchange, String routingKey, UserOrderProducts userOrderProducts);
    public void send(String exchange, String routingKey, OrderWithProducts order);

    public NotificationMessage sendAndReceiveOrder(String exchange, String routingKey, OrderWithProducts order);
}
