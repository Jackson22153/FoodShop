package com.phucx.notification.service.messageQueue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.notification.config.WebSocketConfig;
import com.phucx.notification.constant.WebSocketConstant;
import com.phucx.notification.model.DataDTO;
import com.phucx.notification.model.EventMessage;
import com.phucx.notification.model.Notification;
import com.phucx.notification.service.notification.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageQueueServiceImp implements MessageQueueService{
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public void sendNotification(Notification notification, String exchange, String routingKey) {
        log.info("sendNotification(notification={}, exchange={}, routingKey={})", notification, exchange, routingKey);
        notificationService.createNotification(notification);
        this.rabbitTemplate.convertAndSend(exchange, routingKey, notification);
    }

    @Override
    public void sendMessageToUser(String userID, Notification notificationMessage) {
        log.info("sendMessageToUser(userID={}, notificationMessage={})", userID, notificationMessage.toString());
        // save notification
        notificationService.createNotification(notificationMessage);
        // send notification
        simpMessagingTemplate.convertAndSendToUser(userID, WebSocketConfig.QUEUE_MESSAGES, notificationMessage, getHeaders());
    }
    
    private Map<String, Object> getHeaders(){
        Map<String, Object> header = new HashMap<>();
        header.put("auto-delete", "true");
        header.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
        return header;
    }

    @Override
    public void sendOrderNotificationToEmployeeTopic(Notification notification) {
        notificationService.createNotification(notification);
        // send notification to notification/order topic
        this.simpMessagingTemplate.convertAndSend(WebSocketConstant.TOPIC_EMPLOYEE_NOTIFICAITON_ORDER, notification);
    }

    @Override
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange,
            String routingKey, Class<T> dataType) throws JsonProcessingException {
        log.info("sendAndReceiveData(message={}, exchange={}, routingKey={}, dataType={})", 
            message, exchange, routingKey, dataType.getName());
        String messageJson = objectMapper.writeValueAsString(message);
        String response = (String) rabbitTemplate.convertSendAndReceive(exchange, routingKey, messageJson);
        // convert response message
        JavaType type = objectMapper.getTypeFactory().constructParametricType(EventMessage.class, dataType);
        EventMessage<T>  responseMessage = objectMapper.readValue(response, type);
        return responseMessage;
    }
    @Override
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> message, String exchange,
            String routingKey, TypeReference<T> dataType) throws JsonProcessingException {
        log.info("sendAndReceiveData(message={}, exchange={}, routingKey={}, className={})", 
            message, exchange, routingKey, dataType.toString());
        String messageJson = objectMapper.writeValueAsString(message);
        // send and receive message
        String responseMessage = (String) this.rabbitTemplate.convertSendAndReceive(exchange, routingKey, messageJson);
        // convert message
        TypeReference<EventMessage<T>> typeReference = new TypeReference<EventMessage<T>>() {};
        EventMessage<T> response = objectMapper.readValue(responseMessage, typeReference);
        return response;
    }
}
