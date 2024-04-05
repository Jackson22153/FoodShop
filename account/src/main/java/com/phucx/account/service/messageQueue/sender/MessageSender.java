package com.phucx.account.service.messageQueue.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.account.model.UserOrderProducts;

public interface MessageSender {
    public void send(String exchange, String routingKey, UserOrderProducts userOrderProducts) throws JsonProcessingException;
    
}
