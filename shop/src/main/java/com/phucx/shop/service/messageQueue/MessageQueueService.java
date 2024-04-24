package com.phucx.shop.service.messageQueue;

public interface MessageQueueService {
    public void sendCartNotificationToUser(String userID, String encodedCartJson);

    
}
