package com.phucx.shop.service.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.Customer;

public interface CustomerService {
    public Customer getCustomerByUserID(String userID) throws JsonProcessingException, NotFoundException;
}
