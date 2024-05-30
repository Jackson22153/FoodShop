package com.phucx.account.service.user;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.model.UserRole;

public interface UserService {
    public Page<UserRole> getAllUserRoles(int pageNumber, int pageSize);

    public User getUser(String username);
    public User getUserByID(String userID);

    public boolean resetPassword(String userID);
    
    public boolean assignUserRoles(UserInfo user);

    public UserInfo getUserAuthenticationInfo(String userID);

    public UserInfo getUserInfo(String userID);   
    
    public String getUsername(Authentication authentication);
    public String getUserID(Authentication authentication);

    public String getUserIdOfCustomerID(String customerID);
    public String getUserIdOfEmployeeID(String employeeID);

    public User getUserByCustomerID(String customerID);
    public User getUserByEmployeeID(String employeeID);
    // search users
    public Page<UserRole> searchUsersByUserID(String userID, int pageNumber, int pageSize);
    public Page<UserRole> searchUsersByRoleName(String roleName, int pageNumber, int pageSize);
    public Page<UserRole> searchUsersByUsername(String username, int pageNumber, int pageSize);
    public Page<UserRole> searchUsersByEmail(String email, int pageNumber, int pageSize);
}
