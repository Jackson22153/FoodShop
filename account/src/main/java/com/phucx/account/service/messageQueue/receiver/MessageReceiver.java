package com.phucx.account.service.messageQueue.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.phucx.account.model.UserOrderProducts;

public interface MessageReceiver {
    public void receiver(String order) throws JsonMappingException, JsonProcessingException;
}
