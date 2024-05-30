package com.phucx.order.service.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.EventType;
import com.phucx.order.constant.JwtClaimConstant;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.model.DataRequest;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.User;
import com.phucx.order.model.UserRequest;
import com.phucx.order.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImp implements UserService {
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public String getUsername(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString(JwtClaimConstant.PREFERRED_USERNAME);
        return username;
    }
    @Override
    public String getUserID(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userID = jwt.getSubject();
        return userID;
    }
    @Override
    public User getUserByCustomerID(String customerID) throws JsonProcessingException {
        log.info("getUserByCustomerID(customerID={})", customerID);
        // create a request for user
        UserRequest userRequest = new UserRequest();
        userRequest.setCustomerID(customerID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetUserByCustomerID);
        eventMessage.setPayload(userRequest);
        // receive data
        return sendAndReceiveUserData(eventMessage);
    }
    @Override
    public User getUserByEmployeeID(String employeeID) throws JsonProcessingException{
        log.info("getUserByEmployeeID(employeeID={})", employeeID);
        // create a request for user
        UserRequest userRequest = new UserRequest();
        userRequest.setEmployeeID(employeeID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetUserByEmployeeID);
        eventMessage.setPayload(userRequest);
        // receive data
        return sendAndReceiveUserData(eventMessage);
    }

    // send and receive user data from user queue
    private User sendAndReceiveUserData(EventMessage<DataRequest> eventMessage) throws JsonProcessingException{
        EventMessage<User> response = this.messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.USER_QUEUE, 
            MessageQueueConstant.USER_ROUTING_KEY,
            User.class);
        log.info("response={}", response);
        return response.getPayload();
    }
}
