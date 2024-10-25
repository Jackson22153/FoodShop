package com.phucx.payment.service.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.payment.model.Customer;

public interface CustomerService {
    // customer info
    public Customer getCustomerByID(String customerID) throws JsonProcessingException;
}
