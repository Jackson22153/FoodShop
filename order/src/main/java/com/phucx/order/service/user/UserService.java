package com.phucx.order.service.user;

import org.springframework.security.core.Authentication;

import com.phucx.order.model.User;

public interface UserService {
    public User getUser(String username);
    public User getUserByID(String userID);
    
    public String getUsername(Authentication authentication);
    public String getUserID(Authentication authentication);

    public String getUserIdOfCustomerID(String customerID);
    public String getUserIdOfEmployeeID(String employeeID);
}
