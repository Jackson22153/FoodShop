package com.phucx.order.service.messageQueue.sender;

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

import com.phucx.order.config.MessageQueueConfig;
import com.phucx.order.constant.WebSocketConstant;
import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.service.notification.NotificationService;

@Service
public class MessageSenderImp implements MessageSender{
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private Logger logger = LoggerFactory.getLogger(MessageSenderImp.class);
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
        logger.info("sendMessageToUser(userID={}, notificationMessage={})", userID, notificationMessage.toString());
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
}
