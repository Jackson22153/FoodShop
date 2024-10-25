package com.phucx.shop.service.customer;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.constant.EventType;
import com.phucx.model.CustomerDTO;
import com.phucx.model.DataDTO;
import com.phucx.model.EventMessage;
import com.phucx.shop.constant.MessageQueueConstant;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.Customer;
import com.phucx.shop.service.messageQueue.MessageQueueService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImp implements CustomerService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public Customer getCustomerByUserID(String userID) throws JsonProcessingException, NotFoundException {
        log.info("getCustomerByUserID(userID={})", userID);
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setUserID(userID);
        // fetching customer from account service
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetCustomerByUserID);
        eventMessage.setPayload(customerDTO);
        EventMessage<Customer> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_EXCHANGE, 
            MessageQueueConstant.CUSTOMER_ROUTING_KEY,
            Customer.class);
        
        log.info("Response message: {}", response);

        if(response.getEventType().equals(EventType.NotFoundException)){
            throw new NotFoundException(response.getErrorMessage());
        }

        Customer fetchedCustomer = response.getPayload();
        // receive customer data
        return fetchedCustomer;
    }
    
}
