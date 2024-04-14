package com.phucx.account.service.users;

import org.springframework.security.core.Authentication;

import com.phucx.account.model.UserRolesUtils;
import com.phucx.account.model.Users;

public interface UsersService {
    public Users getUser(String username);
    public Users getUserByID(String userID);

    public boolean createUser(Users user);

    public UserRolesUtils getUserRoles(String userID);   
    
    public String getUsername(Authentication authentication);
    public String getUserID(Authentication authentication);

    public String getUserIdOfCustomerID(String customerID);
    public String getUserIdOfEmployeeID(String employeeID);
}
