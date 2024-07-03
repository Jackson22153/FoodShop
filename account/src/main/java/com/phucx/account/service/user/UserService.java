package com.phucx.account.service.user;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import com.phucx.account.exception.CustomerNotFoundException;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.model.UserRole;

public interface UserService {
    @Cacheable(value = "userrole", key = "#pageNumber")
    public Page<UserRole> getAllUserRoles(int pageNumber, int pageSize);
    @Cacheable(value = "user", key = "#username")
    public User getUser(String username) throws UserNotFoundException;
    @Cacheable(value = "user", key = "#userID")
    public User getUserByID(String userID) throws UserNotFoundException;
    @CacheEvict(cacheNames = {"user", "userrole", "userinfo"}, key = "#userID")
    public boolean resetPassword(String userID) throws UserNotFoundException;
    @CacheEvict(cacheNames = {"user", "userrole", "userinfo"}, key = "#user.user.userID")
    public boolean assignUserRoles(UserInfo user) throws UserNotFoundException;

    @Cacheable(value = "userinfo", key = "#userID")
    public UserInfo getUserInfo(String userID) throws UserNotFoundException;   

    
    public String getUsername(Authentication authentication);
    public String getUserID(Authentication authentication);

    @Cacheable(value = "userstring", key = "#customerID")
    public String getUserIdOfCustomerID(String customerID) throws CustomerNotFoundException;
    @Cacheable(value = "userstring", key = "#employeeID")
    public String getUserIdOfEmployeeID(String employeeID) throws EmployeeNotFoundException;

    @Cacheable(value = "user", key = "#customerID")
    public User getUserByCustomerID(String customerID) throws CustomerNotFoundException;
    @Cacheable(value = "user", key = "#employeeID")
    public User getUserByEmployeeID(String employeeID) throws EmployeeNotFoundException;
    // search users
    @Cacheable(value = "userrole", key = "#userID + ':' + #pageNumber")
    public Page<UserRole> searchUsersByUserID(String userID, int pageNumber, int pageSize);
    @Cacheable(value = "userrole", key = "#roleName + ':' + #pageNumber")
    public Page<UserRole> searchUsersByRoleName(String roleName, int pageNumber, int pageSize);
    @Cacheable(value = "userrole", key = "#username + ':' + #pageNumber")
    public Page<UserRole> searchUsersByUsername(String username, int pageNumber, int pageSize);
    @Cacheable(value = "userrole", key = "#email + ':' + #pageNumber")
    public Page<UserRole> searchUsersByEmail(String email, int pageNumber, int pageSize);
}
