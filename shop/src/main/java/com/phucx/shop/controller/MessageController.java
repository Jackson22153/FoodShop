package com.phucx.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.shop.model.CartCookie;
import com.phucx.shop.service.messageQueue.MessageQueueService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MessageController {
    @Autowired
    private MessageQueueService messageQueueService;

    @MessageMapping("/cart.addItem")
    public void cartItemNotification(@RequestBody CartCookie encodedCartJson, Authentication authentication
    ){
        log.info("cartItemNotification(encodedCartJson={}, authentication={})", encodedCartJson, authentication.getName());
        String userID = authentication.getName();
        messageQueueService.sendCartNotificationToUser(userID, encodedCartJson.getCart());
    }
}
