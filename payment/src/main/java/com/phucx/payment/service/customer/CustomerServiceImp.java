package com.phucx.payment.service.customer;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.constant.EventType;
import com.phucx.model.CustomerDTO;
import com.phucx.model.DataDTO;
import com.phucx.model.EventMessage;
import com.phucx.payment.constant.MessageQueueConstant;
import com.phucx.payment.exception.NotFoundException;
import com.phucx.payment.model.Customer;
import com.phucx.payment.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImp implements CustomerService {
    @Autowired
    private MessageQueueService messageQueueService;
	
	@Override
	public Customer getCustomerByID(String customerID) throws JsonProcessingException {
		log.info("getCustomerByID(customerID={})", customerID);
        // create a request for user
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerID(customerID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetCustomerByID);
        eventMessage.setPayload(customerDTO);
        // receive data
        EventMessage<Customer> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_EXCHANGE, 
            MessageQueueConstant.CUSTOMER_ROUTING_KEY,
            Customer.class);
        log.info("response={}", response);
        // check exception
        if(response.getEventType().equals(EventType.NotFoundException)){
            throw new NotFoundException(response.getErrorMessage());
        }
        return response.getPayload();
	}
}
