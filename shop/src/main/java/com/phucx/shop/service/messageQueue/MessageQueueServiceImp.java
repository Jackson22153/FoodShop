package com.phucx.shop.service.messageQueue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.phucx.shop.config.WebSocketConfig;
import com.phucx.shop.model.NotificationCartCookie;
import com.phucx.shop.service.cookie.CookieService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageQueueServiceImp implements MessageQueueService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    // @Autowired
    // private CookieService cookieService;

    @Override
    public void sendCartNotificationToUser(String userID, String encodedCartJson) {
        log.info("sendCartNotificationToUser(userID={}, encodedCartJson={})", userID, encodedCartJson);
        try {
            // int numberOfCartItems = cookieService.getListProducts(encodedCartJson).size();
            simpMessagingTemplate.convertAndSendToUser(userID, WebSocketConfig.QUEUE_CART, encodedCartJson, getHeaders());
            // log.info("numberOfCartItems: {}", numberOfCartItems); 
            // if(numberOfCartItems>0){
            //     NotificationCartCookie notificationCartCookie = new NotificationCartCookie(userID, userID, numberOfCartItems);
            //     simpMessagingTemplate.convertAndSendToUser(userID, WebSocketConfig.QUEUE_CART, notificationCartCookie, getHeaders());
            // }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, Object> getHeaders(){
        Map<String, Object> header = new HashMap<>();
        header.put("auto-delete", "true");
        header.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
        return header;
    }
    
}
