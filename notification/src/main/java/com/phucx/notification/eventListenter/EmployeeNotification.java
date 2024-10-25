package com.phucx.notification.eventListenter;


import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.constant.NotificationBroadCast;
import com.phucx.model.EventMessage;
import com.phucx.model.UserNotificationDTO;
import com.phucx.notification.config.MessageQueueConfig;
import com.phucx.notification.service.notification.SendUserNotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.NOTIFICATION_EMPLOYEE_QUEUE)
public class EmployeeNotification {
    @Autowired
    private SendUserNotificationService sendUserNotificationService;
    @Autowired
    private ObjectMapper objectMapper;

    // send notification message to employee
    @RabbitHandler
    private void employeeNotificationListener(String message){
        log.info("employeeNotificationListener(message={})", message);
        try {
            TypeReference<EventMessage<UserNotificationDTO>> typeReference = 
                new TypeReference<EventMessage<UserNotificationDTO>>() {};
            EventMessage<UserNotificationDTO> eventMessage = objectMapper.readValue(message, typeReference);
            // process message
            switch (eventMessage.getEventType()) {
                case SendEmployeeNotificationToUser:
                    // send message to user
                    UserNotificationDTO userNotificationDTO = eventMessage.getPayload();
                    if(userNotificationDTO.getReceiverID().equalsIgnoreCase(NotificationBroadCast.ALL_EMPLOYEES.name())){
                        // notification is send to all employees
                        sendUserNotificationService.sendNotificationToAllEmployees(userNotificationDTO);
                    }else {
                        sendUserNotificationService.sendNotificationToEmployee(userNotificationDTO);
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
