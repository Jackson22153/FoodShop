package com.phucx.account.service.messageQueue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.config.WebSocketConfig;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.Notification;
import com.phucx.account.service.notification.NotificationService;

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
    @Override
    public void sendNotification(Notification notification) {
        this.rabbitTemplate.convertAndSend(MessageQueueConfig.NOTIFICATION_QUEUE, 
            MessageQueueConfig.NOTIFICATION_ROUTING_KEY, notification);
    }
    // @Override
    // public Notification sendAndReceiveOrder(OrderWithProducts order) {
    //     // send order to message queue
    //     Notification response = (Notification) this.rabbitTemplate.convertSendAndReceive(
    //         MessageQueueConfig.ORDER_QUEUE, MessageQueueConfig.ORDER_ROUTING_KEY, order);
    //     return response;
    // }
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
    public void sendEmployeeNotificationOrderToTopic(Notification notification) {
        notificationService.createNotification(notification);
        // send notification to notification/order topic
        this.simpMessagingTemplate.convertAndSend(WebSocketConfig.TOPIC_EMPLOYEE_NOTIFICAITON_ORDER, notification);
    }
    @Override
    @SuppressWarnings("unchecked")
    public EventMessage<Object> sendAndReceiveData(Object message, String queueName, String routingKey) {
        log.info("sendAndReceiveData(message={}, queueName={}, routingKey={})", message, queueName, routingKey);
        EventMessage<Object> eventMessage = (EventMessage<Object>) this.rabbitTemplate.convertSendAndReceive(queueName, routingKey, message);
        return eventMessage;
    }
}
