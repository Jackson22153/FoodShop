package com.phucx.keycloakmanagement.service.customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.keycloakmanagement.constant.EventType;
import com.phucx.keycloakmanagement.constant.MessageQueueConstant;
import com.phucx.keycloakmanagement.constant.RoleConstant;
import com.phucx.keycloakmanagement.exception.NotFoundException;
import com.phucx.keycloakmanagement.model.Customer;
import com.phucx.keycloakmanagement.model.CustomerDTO;
import com.phucx.keycloakmanagement.model.CustomerDetails;
import com.phucx.keycloakmanagement.model.DataDTO;
import com.phucx.keycloakmanagement.model.EventMessage;
import com.phucx.keycloakmanagement.model.User;
import com.phucx.keycloakmanagement.service.keycloak.KeycloakService;
import com.phucx.keycloakmanagement.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImp implements CustomerService{
    @Autowired
    private KeycloakService keycloakService;
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public Customer getCustomer(String userID) throws JsonProcessingException, NotFoundException {
        log.info("getCustomer(userID={})", userID);

        CustomerDTO payload = new CustomerDTO();
        payload.setUserID(userID);

        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<DataDTO>(
            eventID, EventType.GetCustomerByUserID, payload);
        
        EventMessage<Customer> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_EXCHANGE, 
            MessageQueueConstant.CUSTOMER_ROUTING_KEY, Customer.class);
        if(response==null || response.getEventType().equals(EventType.NotFoundException)){
            throw new NotFoundException(response.getErrorMessage());
        }
        log.info("Response: {}", response);

        return response.getPayload();
    }

    @Override
    public CustomerDetails getCustomerDetails(String userID) throws JsonProcessingException, NotFoundException {
        log.info("getCustomerDetails(userID={})", userID);
        // fetch customer
        Customer fetchedCustomer = this.getCustomer(userID);
        // fetch appropriate user
        User fetchedUser = keycloakService.getUser(fetchedCustomer.getUserID());

        return new CustomerDetails(fetchedCustomer.getCustomerID(), fetchedCustomer.getUserID(), fetchedCustomer.getContactName(), 
            fetchedCustomer.getAddress(), fetchedCustomer.getCity(), fetchedCustomer.getPhone(), 
            fetchedCustomer.getPicture(), fetchedUser.getUsername(), fetchedUser.getFirstName(), 
            fetchedUser.getLastName(), fetchedUser.getEmail());
        
    }

    @Override
    public List<Customer> getCustomers(List<String> userIDs) throws JsonProcessingException {
        log.info("getCustomers(userIDs={})", userIDs);
        CustomerDTO payload = new CustomerDTO();
        payload.setUserIDs(userIDs);

        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<DataDTO>(
            eventID, EventType.GetCustomersByUserIDs, payload);
        
        TypeReference<EventMessage<List<Customer>>> typeReference = new TypeReference<EventMessage<List<Customer>>>() {};
        EventMessage<List<Customer>> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_EXCHANGE, 
            MessageQueueConstant.CUSTOMER_ROUTING_KEY, typeReference);

        return response.getPayload();
    }

    @Override
    public List<User> getCustomers(Integer pageNumber, Integer pageSize) throws JsonProcessingException {
        log.info("getCustomers(pageNumber={}, pageSize={})", pageNumber, pageSize);
        List<User> users = keycloakService.getUsersByRole(RoleConstant.CUSTOMER.name(), pageNumber, pageSize);
        return this.convertUser(users);
    }

    @Override
    public List<User> searchCustomersByUsername(String username, Integer pageNumber, Integer pageSize) throws JsonProcessingException {
        log.info("searchCustomersByUsername(username={}, pageNumber={}, pageSize={})", username, pageNumber, pageSize);
        List<User> users = keycloakService.searchUsersByUsernameAndRole(username, RoleConstant.CUSTOMER.name(), pageNumber, pageSize);
        return this.convertUser(users);
    }

    @Override
    public List<User> searchCustomersByFirstName(String firstName, Integer pageNumber, Integer pageSize) throws JsonProcessingException {
        log.info("searchCustomersByFirstName(firstName={}, pageNumber={}, pageSize={})", firstName, pageNumber, pageSize);
        List<User> users = keycloakService.searchUsersByFirstNameAndRole(firstName, RoleConstant.CUSTOMER.name(), pageNumber, pageSize);
        return this.convertUser(users);
    }

    @Override
    public List<User> searchCustomersByLastName(String lastName, Integer pageNumber, Integer pageSize) throws JsonProcessingException {
        log.info("searchCustomersByLastName(lastName={}, pageNumber={}, pageSize={})", lastName, pageNumber, pageSize);
        List<User> users = keycloakService.searchUsersByLastNameAndRole(lastName, RoleConstant.CUSTOMER.name(), pageNumber, pageSize);
        return this.convertUser(users);
    }

    @Override
    public List<User> searchCustomersByEmail(String email, Integer pageNumber, Integer pageSize) throws JsonProcessingException {
        log.info("searchCustomersByEmail(email={}, pageNumber={}, pageSize={})", email, pageNumber, pageSize);
        List<User> users = keycloakService.searchUsersByEmailAndRole(email, RoleConstant.CUSTOMER.name(), pageNumber, pageSize);
        return this.convertUser(users);
    }

    private List<User> convertUser(List<User> users) throws JsonProcessingException{
        List<String> userIds = users.stream().map(User::getUserID).collect(Collectors.toList());
        List<Customer> customers = this.getCustomers(userIds);
        
        for (User user : users) {
            Customer fetchedCustomer = this.findCustomer(customers, user.getUserID()).orElse(new Customer());
            user.setPicture(fetchedCustomer.getPicture());
        }
        return users;
    }

    private Optional<Customer> findCustomer(List<Customer> customers, String userID){
        return customers.stream().filter(customer -> customer.getUserID().equals(userID)).findFirst();
    }
}
