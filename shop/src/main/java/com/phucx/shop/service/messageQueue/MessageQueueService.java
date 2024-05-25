package com.phucx.shop.service.messageQueue;

public interface MessageQueueService {
    public Object sendAndReceiveData(Object message, String queueName, String routingKey);
}
