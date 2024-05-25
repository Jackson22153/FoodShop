package com.phucx.shop.service.customer;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.shop.constant.EventType;
import com.phucx.shop.constant.MessageQueueConstant;
import com.phucx.shop.model.Customer;
import com.phucx.shop.model.EventMessage;
import com.phucx.shop.model.UserRequest;
import com.phucx.shop.service.messageQueue.MessageQueueService;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImp implements CustomerService{
    @Autowired
    private MessageQueueService messageQueueService;
 
    @Override
    public Customer getCustomerByUserID(String userID) {
        log.info("getCustomerByUserID(userID={})", userID);
        UserRequest userRequest = new UserRequest();
        userRequest.setUserID(userID);

        String eventID = UUID.randomUUID().toString();
        // fetching customer from account service
        EventMessage<UserRequest> eventMessage = new EventMessage<>(eventID, EventType.GetCustomerByUserID, userRequest);
        Customer fetchedCustomer = (Customer) messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_QUEUE, 
            MessageQueueConstant.ACCOUNT_ROUTING_KEY);
        // receive customer data
        if(fetchedCustomer==null) throw new NotFoundException("Customer with userID " + userID + " does not found");
        return fetchedCustomer;
    }
    
}
