package com.phucx.account.service.messageQueue.sender;

import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.UserOrderProducts;

public interface MessageSender {
    public void send(String exchange, String routingKey, UserOrderProducts userOrderProducts);
    public void send(String exchange, String routingKey, OrderWithProducts order);

    public Integer sendAndReceiveOrder(String exchange, String routingKey, OrderWithProducts order);
}
