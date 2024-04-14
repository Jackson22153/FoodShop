package com.phucx.account.service.messageQueue.sender;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.phucx.account.config.WebSocketConfig;
import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderWithProducts;

@Service
public class MessageSenderImp implements MessageSender{
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private Logger logger = LoggerFactory.getLogger(MessageSenderImp.class);
    @Override
    public void send(String exchange, String routingKey, OrderWithProducts order) {
        this.rabbitTemplate.convertAndSend(exchange, routingKey, order);
    }
    @Override
    public NotificationMessage sendAndReceiveOrder(String exchange, String routingKey, OrderWithProducts order) {
        NotificationMessage response = (NotificationMessage) this.rabbitTemplate.convertSendAndReceive(exchange, routingKey, order);
        return response;
    }
    @Override
    public void sendMessageToUser(String userID, NotificationMessage notificationMessage) {
        logger.info("sendMEssageToUser(userID={}, notificationMessage={})", userID, notificationMessage.toString());
        simpMessagingTemplate.convertAndSendToUser(userID, WebSocketConfig.QUEUE_MESSAGES, notificationMessage, getHeaders());
    }
    
    private Map<String, Object> getHeaders(){
        Map<String, Object> header = new HashMap<>();
        header.put("auto-delete", "true");
        header.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
        return header;
    }
}
