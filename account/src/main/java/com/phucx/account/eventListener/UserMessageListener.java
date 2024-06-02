package com.phucx.account.eventListener;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.constant.EventType;
import com.phucx.account.model.CustomerDTO;
import com.phucx.account.model.EmployeeDTO;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.User;
import com.phucx.account.model.UserDTO;
import com.phucx.account.service.user.UserService;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.USER_QUEUE)
public class UserMessageListener {
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitHandler
    public String fetchUser(String message){
        log.info("fetchUser({})", message);
        // create response message
        String eventID = UUID.randomUUID().toString();
        EventMessage<Object> responseMessage = new EventMessage<>();
        responseMessage.setEventId(eventID);
        try {
            TypeReference<EventMessage<UserDTO>> typeRef = new TypeReference<EventMessage<UserDTO>>() {};
            EventMessage<UserDTO> userDTO = objectMapper.readValue(message, typeRef);
            UserDTO payload = userDTO.getPayload();
            // fetch data
            if(userDTO.getEventType().equals(EventType.GetUserByCustomerID)){
                // get user by customerID
                String customerID = ((CustomerDTO) payload).getCustomerID();
                User fetchedUser = userService.getUserByCustomerID(customerID);
                // set response message
                responseMessage.setPayload(fetchedUser);
                responseMessage.setEventType(EventType.ReturnUserByCustomerID);
            }else if(userDTO.getEventType().equals(EventType.GetUserByEmployeeID)){
                // get user by employeeID
                String employeeID = ((EmployeeDTO) payload).getEmployeeID();
                User fetchedUser = userService.getUserByEmployeeID(employeeID);
                // set response message
                responseMessage.setPayload(fetchedUser);
                responseMessage.setEventType(EventType.ReturnUserByEmployeeID);
            }
            String response = objectMapper.writeValueAsString(responseMessage);
            return response;
        } catch (Exception e) {
           log.error("Error: {}", e.getMessage());
           return null;
        }
    }
}
