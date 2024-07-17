package com.phucx.notification.eventListenter;


import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.notification.config.MessageQueueConfig;
import com.phucx.notification.constant.NotificationBroadCast;
import com.phucx.notification.model.EventMessage;
import com.phucx.notification.model.UserNotificationDTO;
import com.phucx.notification.service.notification.SendUserNotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.NOTIFICATION_CUSTOMER_QUEUE)
public class CustomerNotification {
    @Autowired
    private SendUserNotificationService sendUserNotificationService;
    @Autowired
    private ObjectMapper objectMapper;

    // send notification message to customer
    @RabbitHandler
    private void customerNotificationListener(String message){
        log.info("customerNotificationListener(message={})", message);
        try {
            TypeReference<EventMessage<UserNotificationDTO>> typeReference = new TypeReference<EventMessage<UserNotificationDTO>>() {};
            EventMessage<UserNotificationDTO> eventMessage = objectMapper.readValue(message, typeReference);
            // process message
            switch (eventMessage.getEventType()) {
                case SendCustomerNotificationToUser:
                    // send message to a user
                    UserNotificationDTO userNotificationDTO = eventMessage.getPayload();
                    if(userNotificationDTO.getReceiverID().equalsIgnoreCase(NotificationBroadCast.ALL_CUSTOMERS.name())){
                        // notification is send to all customers
                        sendUserNotificationService.sendNotificationToAllCustomers(userNotificationDTO);
                    }else {
                        // send notification to a specific customer
                        sendUserNotificationService.sendNotificationToCustomer(userNotificationDTO);
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
