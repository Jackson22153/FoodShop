package com.phucx.shop.service.customer;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.shop.constant.EventType;
import com.phucx.shop.constant.MessageQueueConstant;
import com.phucx.shop.model.Customer;
import com.phucx.shop.model.DataDTO;
import com.phucx.shop.model.EventMessage;
import com.phucx.shop.model.UserDTO;
import com.phucx.shop.service.messageQueue.MessageQueueService;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImp implements CustomerService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public Customer getCustomerByUserID(String userID) throws JsonProcessingException {
        log.info("getCustomerByUserID(userID={})", userID);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserID(userID);
    
        String eventID = UUID.randomUUID().toString();
        // fetching customer from account service
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetCustomerByUserID);
        eventMessage.setPayload(userDTO);
        EventMessage<Customer> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.USER_QUEUE, 
            MessageQueueConstant.USER_ROUTING_KEY,
            Customer.class);
        
        log.info("Response message: {}", response);
        Customer fetchedCustomer = response.getPayload();
        // receive customer data
        if(fetchedCustomer==null) throw new NotFoundException("Customer with userID " + userID + " does not found");
        return fetchedCustomer;
    }
    
}
