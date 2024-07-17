package com.phucx.order.service.notification;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.EventType;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.NotificationDTO;
import com.phucx.order.model.OrderNotificationDTO;
import com.phucx.order.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationServiceImp implements NotificationService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public void markAsReadForConfirmedOrderNotification(OrderNotificationDTO notification) throws JsonProcessingException {
        log.info("markAsReadForConfirmedOrderNotification({})", notification); 
        this.sendMessage(notification, EventType.MarkOrderAsConfirmed, 
            MessageQueueConstant.NOTIFICATION_EMPLOYEE_ORDER_ROUTING_KEY);
    }

    private void sendMessage(OrderNotificationDTO notification, EventType eventType, String routingKey) throws JsonProcessingException{
        // create an event message
        String eventID = UUID.randomUUID().toString();
        EventMessage<NotificationDTO> eventMessage = new EventMessage<NotificationDTO>(
            eventID, eventType, notification);
        // send message 
        messageQueueService.sendNotification(eventMessage, MessageQueueConstant.NOTIFICATION_EXCHANGE, routingKey); 
    }

    @Override
    public void sendCustomerOrderNotification(OrderNotificationDTO notification) throws JsonProcessingException {
        log.info("sendCustomerOrderNotification({})", notification);
        this.sendMessage(notification, EventType.SendOrderNotificationToUser, 
            MessageQueueConstant.NOTIFICATION_CUSTOMER_ORDER_ROUTING_KEY);
    }

    @Override
    public void sendEmployeeOrderNotification(OrderNotificationDTO notification) throws JsonProcessingException {
        log.info("sendEmployeeOrderNotification({})", notification);
        this.sendMessage(notification, EventType.SendOrderNotificationToUser, 
            MessageQueueConstant.NOTIFICATION_EMPLOYEE_ORDER_ROUTING_KEY);
    }

}
