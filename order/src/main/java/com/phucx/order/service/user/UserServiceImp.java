package com.phucx.order.service.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.phucx.order.constant.EventType;
import com.phucx.order.constant.JwtClaimConstant;
import com.phucx.order.constant.MessageQueueConstant;
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
    // @Override
    // public User getUser(String username) {
    //     User user = userRepository.findByUsername(username)
    //         .orElseThrow(()-> new NotFoundException("User " + username + " does not found"));
    //     return user;
    // }
    // @Override
    // public User getUserByID(String userID) {
    //     User user = userRepository.findById(userID)
    //         .orElseThrow(()-> new NotFoundException("User " + userID + " does not found"));
    //     return user;
    // }

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
    public User getUserByCustomerID(String customerID) {
        log.info("getUserByCustomerID(customerID={})", customerID);
        // create a request for user
        UserRequest userRequest = new UserRequest();
        userRequest.setCustomerID(customerID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<UserRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetUserByCustomerID);
        eventMessage.setPayload(userRequest);
        // receive data
        EventMessage<Object> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_QUEUE, 
            MessageQueueConstant.ACCOUNT_ROUTING_KEY);
        log.info("response={}", response);
        return (User) response.getPayload();
    }
    @Override
    public User getUserByEmployeeID(String employeeID) {
        log.info("getUserByEmployeeID(employeeID={})", employeeID);
        // create a request for user
        UserRequest userRequest = new UserRequest();
        userRequest.setEmployeeID(employeeID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<UserRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetUserByEmployeeID);
        eventMessage.setPayload(userRequest);
        // receive data
        EventMessage<Object> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_QUEUE, 
            MessageQueueConstant.ACCOUNT_ROUTING_KEY);
        log.info("response={}", response);
        return (User) response.getPayload();
    }
}
