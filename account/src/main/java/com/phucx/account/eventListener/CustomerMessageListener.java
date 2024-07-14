package com.phucx.account.eventListener;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.constant.EventType;
import com.phucx.account.exception.CustomerNotFoundException;
import com.phucx.account.exception.InvalidUserException;
import com.phucx.account.model.CustomerDTO;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.UserProfile;
import com.phucx.account.service.customer.CustomerService;
import com.phucx.account.service.user.UserProfileService;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.CUSTOMER_QUEUE)
public class CustomerMessageListener {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitHandler
    public String fetchCustomer(String message){
        log.info("fetchCustomer({})", message);
        // create response message
        EventMessage<Object> responseMessage = createResponseMessage(Object.class);
        try {
            TypeReference<EventMessage<CustomerDTO>> typeRef = new TypeReference<EventMessage<CustomerDTO>>() {};
            EventMessage<CustomerDTO> customerDTO = objectMapper.readValue(message, typeRef);
            CustomerDTO payload = customerDTO.getPayload();
            // fetch data
            if(customerDTO.getEventType().equals(EventType.GetCustomerByID)){
                // get customer by id
                String customerID = payload.getCustomerID();
                CustomerDetail fetchedCustomer = customerService.getCustomerByID(customerID);
                // set response message
                responseMessage.setPayload(fetchedCustomer);
                responseMessage.setEventType(EventType.ReturnCustomerByID);
            }else if(customerDTO.getEventType().equals(EventType.GetCustomersByIDs)){
                // get customers by ids
                List<String> customerIDs = payload.getCustomerIDs();
                List<CustomerDetail> fetchedCustomers = customerService.getCustomersByIDs(customerIDs);
                // set response message
                responseMessage.setPayload(fetchedCustomers);
                responseMessage.setEventType(EventType.ReturnCustomersByIDs);
            }else if(customerDTO.getEventType().equals(EventType.GetCustomerByUserID)){
                // get customer by userID
                String userID = payload.getUserID();
                CustomerDetail fetchedCustomer = customerService.getCustomerByUserID(userID);
                // set response message
                responseMessage.setPayload(fetchedCustomer);
                responseMessage.setEventType(EventType.ReturnCustomerByUserID);
            }else if(customerDTO.getEventType().equals(EventType.GetCustomersByUserIDs)){
                // get customers by userIDs
                List<String> userIDs = payload.getUserIDs();
                List<CustomerDetail> fetchedCustomers = customerService.getCustomersByUserIDs(userIDs);
                // set response message
                responseMessage.setPayload(fetchedCustomers);
                responseMessage.setEventType(EventType.ReturnCustomersByUserIDs);
            }else if(customerDTO.getEventType().equals(EventType.GetUserByCustomerID)){
                // get user by customerID
                String customerID = payload.getCustomerID();
                UserProfile fetchedUser = userProfileService.getUserProfileByCustomerID(customerID);
                // set response message
                responseMessage.setPayload(fetchedUser);
                responseMessage.setEventType(EventType.ReturnUserByCustomerID);
            }else if(customerDTO.getEventType().equals(EventType.CreateCustomerDetail)){
                // create customer detail
                String contactName = payload.getContactName();
                String userID = payload.getUserID();
                CustomerDetail newCustomer = customerService.addNewCustomer(new CustomerDetail(userID, contactName));
                responseMessage.setPayload(newCustomer);
                responseMessage.setEventType(EventType.ReturnCreateCustomerDetail);
            }
            String response = objectMapper.writeValueAsString(responseMessage);
            return response;
        } catch (JsonProcessingException e){
            log.error("Error: {}", e.getMessage());
            return null;
        } catch (CustomerNotFoundException e) {
            log.error("Error: {}", e.getMessage());
            return handleNotFoundException(responseMessage, e.getMessage());
        } catch (InvalidUserException e){
            log.error("Error: {}", e.getMessage());
            return handleInvalidUserException(responseMessage, e.getMessage());
        }
    }

    private String handleInvalidUserException(EventMessage<Object> responseMessage, String errorMessage){
        try {
            responseMessage.setErrorMessage(errorMessage);
            responseMessage.setEventType(EventType.InvalidUserException);
            String responsemessage = objectMapper.writeValueAsString(responseMessage);
            return responsemessage;
        } catch (Exception exception) {
            log.error("Error: {}", exception.getMessage());
            return null;
        }
    }

    private String handleNotFoundException(EventMessage<Object> responseMessage, String errorMessage){
        try {
            responseMessage.setErrorMessage(errorMessage);
            responseMessage.setEventType(EventType.NotFoundException);
            String responsemessage = objectMapper.writeValueAsString(responseMessage);
            return responsemessage;
        } catch (Exception exception) {
            log.error("Error: {}", exception.getMessage());
            return null;
        }
    }

    private <T> EventMessage<T> createResponseMessage(Class<T> type){
        EventMessage<T> responseMessage = new EventMessage<>();
        String eventID = UUID.randomUUID().toString();
        responseMessage.setEventId(eventID);
        return responseMessage;
    }
}
