package com.phucx.account.service.user;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import com.phucx.account.exception.CustomerNotFoundException;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.model.UserRole;

public interface UserService {
    public Page<UserRole> getAllUserRoles(int pageNumber, int pageSize);

    public User getUser(String username) throws UserNotFoundException;
    public User getUserByID(String userID) throws UserNotFoundException;
    
    public boolean resetPassword(String userID) throws UserNotFoundException;
    
    public boolean assignUserRoles(UserInfo user) throws UserNotFoundException;
    
    public UserInfo getUserAuthenticationInfo(String userID) throws UserNotFoundException;
    public UserInfo getUserInfo(String userID) throws UserNotFoundException;   

    
    public String getUsername(Authentication authentication);
    public String getUserID(Authentication authentication);

    public String getUserIdOfCustomerID(String customerID) throws CustomerNotFoundException;
    public String getUserIdOfEmployeeID(String employeeID) throws EmployeeNotFoundException;

    public User getUserByCustomerID(String customerID) throws CustomerNotFoundException;
    public User getUserByEmployeeID(String employeeID) throws EmployeeNotFoundException;
    // search users
    public Page<UserRole> searchUsersByUserID(String userID, int pageNumber, int pageSize);
    public Page<UserRole> searchUsersByRoleName(String roleName, int pageNumber, int pageSize);
    public Page<UserRole> searchUsersByUsername(String username, int pageNumber, int pageSize);
    public Page<UserRole> searchUsersByEmail(String email, int pageNumber, int pageSize);
}
