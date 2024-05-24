package com.phucx.shop.service.messageQueue;

import com.phucx.shop.model.EventMessage;

public interface MessageQueueService {
    public Object sendAndReceiveData(EventMessage message);
}
