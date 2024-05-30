package com.phucx.account.eventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketEventListener {
    private Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        logger.info("handleSessionSubscribeEvent ", event.getMessage().toString());
        Message<?> message = (Message<?>) event.getMessage();
        String simpDestination = (String) message.getHeaders().get("simpDestination");
        System.out.println(simpDestination);
    }

    
}
