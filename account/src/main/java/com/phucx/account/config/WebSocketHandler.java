package com.phucx.account.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// @Order(Ordered.HIGHEST_PRECEDENCE+99)
// @Component
public class WebSocketHandler extends TextWebSocketHandler {
    private Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
       String username = (String) session.getAttributes().get("username");
       super.afterConnectionEstablished(session);
    }


}
