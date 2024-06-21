package com.phucx.order.service.customer;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.Customer;

public interface CustomerService {
    // customer info
    public Customer getCustomerByID(String customerID) throws JsonProcessingException, NotFoundException;
    public List<Customer> getCustomersByIDs(List<String> customerIDs) throws JsonProcessingException, NotFoundException;
    public Customer getCustomerByUserID(String userID) throws JsonProcessingException, NotFoundException;
}
