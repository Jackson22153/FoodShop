package com.phucx.keycloakmanagement.service.customer;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.keycloakmanagement.exception.NotFoundException;
import com.phucx.keycloakmanagement.model.Customer;
import com.phucx.keycloakmanagement.model.CustomerDetails;
import com.phucx.keycloakmanagement.model.User;

public interface CustomerService {
    public Customer getCustomer(String userID) throws JsonProcessingException, NotFoundException;
    public List<Customer> getCustomers(List<String> userIDs) throws JsonProcessingException;
    public CustomerDetails getCustomerDetails(String userID) throws JsonProcessingException, NotFoundException;

    public List<User> getCustomers(Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public List<User> searchCustomersByUsername(String username, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public List<User> searchCustomersByFirstName(String firstName, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public List<User> searchCustomersByLastName(String lastName, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public List<User> searchCustomersByEmail(String email, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
}
