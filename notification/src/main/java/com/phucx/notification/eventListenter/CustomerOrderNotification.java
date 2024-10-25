package com.phucx.notification.eventListenter;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.constant.NotificationBroadCast;
import com.phucx.model.EventMessage;
import com.phucx.model.OrderNotificationDTO;
import com.phucx.notification.config.MessageQueueConfig;
import com.phucx.notification.service.notification.SendOrderNotificationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.NOTIFICATION_CUSTOMER_ORDER_QUEUE)
public class CustomerOrderNotification {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SendOrderNotificationService sendOrderNotificationService;

    // send order notification message to customer
    @RabbitHandler
    public void notificationCustomerOrder(String message){
        log.info("notificationCustomerOrder(message={})", message);
        try {
             // extract notification from message
            TypeReference<EventMessage<OrderNotificationDTO>> typeReference = 
                new TypeReference<EventMessage<OrderNotificationDTO>>() {};
            EventMessage<OrderNotificationDTO> eventMessage = objectMapper.readValue(message, typeReference);
            OrderNotificationDTO orderNotificationDTO = eventMessage.getPayload();
            // process message
            switch (eventMessage.getEventType()) {
                case SendOrderNotificationToUser:
                    // send message to user
                    if(orderNotificationDTO.getReceiverID().equalsIgnoreCase(NotificationBroadCast.ALL_CUSTOMERS.name())){
                        // send notification message to all customers
                        sendOrderNotificationService.sendNotificationToAllCustomers(orderNotificationDTO);
                    }else {
                        // send notification message to a specific user
                        sendOrderNotificationService.sendNotificationToCustomer(orderNotificationDTO);  
                    }
                    break;
                default:

                    break;
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}
