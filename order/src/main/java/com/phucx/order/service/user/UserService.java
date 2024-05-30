package com.phucx.order.service.user;

import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.model.User;

public interface UserService {
    public String getUsername(Authentication authentication);
    public String getUserID(Authentication authentication);

    public User getUserByCustomerID(String customerID) throws JsonProcessingException;
    public User getUserByEmployeeID(String employeeID) throws JsonProcessingException;
}
