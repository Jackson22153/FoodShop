package com.phucx.account.eventListener;

import java.util.List;
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
import com.phucx.account.model.Customer;
import com.phucx.account.model.CustomerDTO;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.User;
import com.phucx.account.service.customer.CustomerService;
import com.phucx.account.service.user.UserService;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.CUSTOMER_QUEUE)
public class CustomerMessageListener {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitHandler
    public String fetchCustomer(String message){
        log.info("fetchCustomer({})", message);
        // create response message
        String eventID = UUID.randomUUID().toString();
        EventMessage<Object> responseMessage = new EventMessage<>();
        responseMessage.setEventId(eventID);
        try {
            TypeReference<EventMessage<CustomerDTO>> typeRef = new TypeReference<EventMessage<CustomerDTO>>() {};
            EventMessage<CustomerDTO> customerDTO = objectMapper.readValue(message, typeRef);
            CustomerDTO payload = customerDTO.getPayload();
            // fetch data
            if(customerDTO.getEventType().equals(EventType.GetCustomerByID)){
                // get customer by id
                String customerID = payload.getCustomerID();
                Customer fetchedCustomer = customerService.getCustomerByID(customerID);
                // set response message
                responseMessage.setPayload(fetchedCustomer);
                responseMessage.setEventType(EventType.ReturnCustomerByID);
            }else if(customerDTO.getEventType().equals(EventType.GetCustomersByIDs)){
                // get customers by ids
                List<String> customerIDs = payload.getCustomerIDs();
                List<Customer> fetchedCustomers = customerService.getCustomersByIDs(customerIDs);
                // set response message
                responseMessage.setPayload(fetchedCustomers);
                responseMessage.setEventType(EventType.ReturnCustomersByIDs);
            }else if(customerDTO.getEventType().equals(EventType.GetCustomerByUserID)){
                // get customer by userID
                String userID = payload.getUserID();
                Customer fetchedCustomer = customerService.getCustomerByUserID(userID);
                // set response message
                responseMessage.setPayload(fetchedCustomer);
                responseMessage.setEventType(EventType.ReturnCustomerByUserID);
            }else if(customerDTO.getEventType().equals(EventType.GetUserByCustomerID)){
                // get user by customerID
                String customerID = payload.getCustomerID();
                User fetchedUser = userService.getUserByCustomerID(customerID);
                // set response message
                responseMessage.setPayload(fetchedUser);
                responseMessage.setEventType(EventType.ReturnUserByCustomerID);
            }
            String response = objectMapper.writeValueAsString(responseMessage);
            return response;
        } catch (Exception e) {
           log.error("Error: {}", e.getMessage());
           return null;
        }
    }
}
