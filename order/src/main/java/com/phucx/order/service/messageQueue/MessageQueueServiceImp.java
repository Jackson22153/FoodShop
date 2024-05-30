package com.phucx.order.service.messageQueue;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.order.config.MessageQueueConfig;
import com.phucx.order.constant.WebSocketConstant;
import com.phucx.order.model.DataDTO;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.service.notification.NotificationService;

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
    public void sendNotification(Notification notification) {
        this.rabbitTemplate.convertAndSend(MessageQueueConfig.ORDER_NOTIFICATION_QUEUE, 
            MessageQueueConfig.ORDER_NOTIFICATION_ROUTING_KEY, notification);
    }
    @Override
    public Notification sendAndReceiveOrder(OrderWithProducts order) {
        // send order to message queue
        Notification response = (Notification) this.rabbitTemplate.convertSendAndReceive(
            MessageQueueConfig.ORDER_QUEUE, MessageQueueConfig.ORDER_ROUTING_KEY, order);
        return response;
    }
    @Override
    public void sendNotificationToUser(String userID, Notification notificationMessage) {
        log.info("sendMessageToUser(userID={}, notificationMessage={})", userID, notificationMessage.toString());
        // save notification
        notificationService.createNotification(notificationMessage);
        // send notification
        simpMessagingTemplate.convertAndSendToUser(userID, WebSocketConstant.QUEUE_MESSAGES, notificationMessage, getHeaders());
    }
    
    private Map<String, Object> getHeaders(){
        Map<String, Object> header = new HashMap<>();
        header.put("auto-delete", "true");
        header.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
        return header;
    }
    @Override
    public void sendEmployeeNotificationOrderToTopic(Notification notification) {
        notificationService.createNotification(notification);
        // send notification to notification/order topic
        this.simpMessagingTemplate.convertAndSend(WebSocketConstant.TOPIC_EMPLOYEE_NOTIFICAITON_ORDER, notification);
    }

    @Override
    public void sendMessageToUser(String userID, Notification notificationMessage) {
        log.info("sendMessageToUser(userID={}, notificationMessage={})", userID, notificationMessage.toString());
        // save notification
        notificationService.createNotification(notificationMessage);
        // send notification
        simpMessagingTemplate.convertAndSendToUser(userID, WebSocketConstant.QUEUE_MESSAGES, notificationMessage, getHeaders());
    }

    @Override
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> eventMessage, String queueName,
            String routingKey, Class<T> dataType) throws JsonProcessingException {
        log.info("sendAndReceiveData(eventMessage={}, queueName={}, routingKey={}, className={})", 
            eventMessage, queueName, routingKey, dataType.getName());
        String message = objectMapper.writeValueAsString(eventMessage);
        // send and receive message
        String responseMessage = (String) this.rabbitTemplate.convertSendAndReceive(queueName, routingKey, message);
        // convert message
        TypeReference<EventMessage<T>> typeReference = new TypeReference<EventMessage<T>>() {};
        EventMessage<T> response = objectMapper.readValue(responseMessage, typeReference);
        return response;
    }
    @Override
    public <T> EventMessage<T> sendAndReceiveData(EventMessage<DataDTO> eventMessage, String queueName,
            String routingKey, TypeReference<T> dataType) throws JsonProcessingException {
        log.info("sendAndReceiveData(eventMessage={}, queueName={}, routingKey={}, className={})", 
        eventMessage, queueName, routingKey, dataType.toString());
        String message = objectMapper.writeValueAsString(eventMessage);
        // send and receive message
        String responseMessage = (String) this.rabbitTemplate.convertSendAndReceive(queueName, routingKey, message);
        // convert message
        TypeReference<EventMessage<T>> typeReference = new TypeReference<EventMessage<T>>() {};
        EventMessage<T> response = objectMapper.readValue(responseMessage, typeReference);
        return response;
    }
}
