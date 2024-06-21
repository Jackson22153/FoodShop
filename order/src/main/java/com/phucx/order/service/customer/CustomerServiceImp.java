package com.phucx.order.service.customer;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.order.constant.EventType;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.CustomerDTO;
import com.phucx.order.model.DataDTO;
import com.phucx.order.model.EventMessage;
import com.phucx.order.service.messageQueue.MessageQueueService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImp implements CustomerService {
    @Autowired
    private MessageQueueService messageQueueService;
	
	@Override
	public Customer getCustomerByID(String customerID) throws JsonProcessingException, NotFoundException {
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

    @Override
    public List<Customer> getCustomersByIDs(List<String> customerIDs) throws JsonProcessingException, NotFoundException{
        log.info("getCustomersByIDs(customerIDs={})", customerIDs);
        // create a request for user
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerIDs(customerIDs);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetCustomersByIDs);
        eventMessage.setPayload(customerDTO);
        // receive data
        TypeReference<EventMessage<List<Customer>>> typeReference = 
            new TypeReference<EventMessage<List<Customer>>>() {};
        EventMessage<List<Customer>> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_EXCHANGE, 
            MessageQueueConstant.CUSTOMER_ROUTING_KEY,
            typeReference);
        log.info("response={}", response);
        // check exception
        if(response.getEventType().equals(EventType.NotFoundException)){
            throw new NotFoundException(response.getErrorMessage());
        }
        return response.getPayload();
    }

    @Override
    public Customer getCustomerByUserID(String userID) throws JsonProcessingException, NotFoundException {
        log.info("getCustomerByUserID(userID={})", userID);
        // create a request for user
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setUserID(userID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetCustomerByUserID);
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
