package com.phucx.order.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.phucx.order.constant.JwtClaimConstant;
import com.phucx.order.model.CustomerAccount;
import com.phucx.order.model.EmployeeAccount;
import com.phucx.order.model.User;
import com.phucx.order.repository.CustomerAccountRepository;
import com.phucx.order.repository.EmployeeAccountRepository;
import com.phucx.order.repository.UserRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerAccountRepository customerAccountRepository;
    @Autowired
    private EmployeeAccountRepository employeeAccountRepository;
    
    @Override
    public User getUser(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(()-> new NotFoundException("User " + username + " does not found"));
        return user;
    }
    @Override
    public User getUserByID(String userID) {
        User user = userRepository.findById(userID)
            .orElseThrow(()-> new NotFoundException("User " + userID + " does not found"));
        return user;
    }

    @Override
    public String getUsername(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString(JwtClaimConstant.PREFERRED_USERNAME);
        return username;
    }
    @Override
    public String getUserID(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userID = jwt.getSubject();
        return userID;
    }
    @Override
    public String getUserIdOfCustomerID(String customerID) {
        CustomerAccount customerAccount = customerAccountRepository.findByCustomerID(customerID)
            .orElseThrow(()-> new NotFoundException("Customer "+customerID+" does not found"));
        return customerAccount.getUserID();
    }
    @Override
    public String getUserIdOfEmployeeID(String employeeID) {
        EmployeeAccount employeeAccount = employeeAccountRepository.findByEmployeeID(employeeID)
            .orElseThrow(()-> new NotFoundException("Employee "+ employeeID +" does not found"));
        return employeeAccount.getUserID();
    }
}
