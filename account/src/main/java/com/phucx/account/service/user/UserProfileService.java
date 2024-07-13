package com.phucx.account.service.user;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;

import com.phucx.account.exception.CustomerNotFoundException;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.UserAuthentication;
import com.phucx.account.model.UserProfile;

public interface UserProfileService {
    @Cacheable(value = "userinfo", key = "#userID")
    public UserProfile getUserProfile(String userID) throws UserNotFoundException;
    @Cacheable(value = "userinfo", key = "#profileID")
    public UserProfile getUserProfileByID(String profileID) throws UserNotFoundException;

    public UserAuthentication getUserAuthentication(Authentication authentication) throws UserNotFoundException;

    public String getUsername(Authentication authentication);
    public String getUserID(Authentication authentication);

    @Cacheable(value = "userinfo", key = "#customerID")
    public UserProfile getUserProfileByCustomerID(String customerID) throws CustomerNotFoundException;
    @Cacheable(value = "userinfo", key = "#employeeID")
    public UserProfile getUserProfileByEmployeeID(String employeeID) throws EmployeeNotFoundException;
}
