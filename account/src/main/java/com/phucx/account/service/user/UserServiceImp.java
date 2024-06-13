package com.phucx.account.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.phucx.account.config.WebConfig;
import com.phucx.account.constant.RoleConstant;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.Role;
import com.phucx.account.model.UserRole;
import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.repository.CustomerAccountRepository;
import com.phucx.account.repository.EmployeeAccountRepository;
import com.phucx.account.repository.RoleRepository;
import com.phucx.account.repository.UserRoleRepository;
import com.phucx.account.repository.UserRepository;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
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
    public UserInfo getUserInfo(String userID) {
        log.info("getUserInfo(userID={})", userID);
        List<UserRole> userRoles = userRoleRepository.findByUserID(userID);
        if(userRoles==null || userRoles.isEmpty()){
            throw new NotFoundException("User " + userID + " does not found");
        }
        UserRole firstEntity = userRoles.get(0);
        log.info("firstEntity: {}", firstEntity);
        User user = new User(firstEntity.getUserID(), firstEntity.getUsername(), firstEntity.getEmail());
        List<Role> roles = userRoles.stream().map(userRole ->{
            return new Role(userRole.getRoleID(), userRole.getRoleName());
        }).collect(Collectors.toList());
        log.info("roles: {}", roles);
        return new UserInfo(user, roles);
    }
    @Override
    public String getUsername(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString(WebConfig.PREFERRED_USERNAME);
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
    @Override
    public Page<UserRole> getAllUserRoles(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRoleRepository.findAll(pageable);
    }
    @Override
    public Page<UserRole> searchUsersByUserID(String userID, int pageNumber, int pageSize) {
        String searchParam = "%" + userID +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<UserRole> users = userRoleRepository.findByUserIDLike(searchParam, page);
        return users;
    }
    @Override
    public Page<UserRole> searchUsersByRoleName(String roleName, int pageNumber, int pageSize) {
        String searchParam = "%" + roleName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<UserRole> users = userRoleRepository.findByRoleNameLike(searchParam, page);
        return users;
    }
    @Override
    public Page<UserRole> searchUsersByUsername(String username, int pageNumber, int pageSize) {
        String searchParam = "%" + username +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<UserRole> users = userRoleRepository.findByUsernameLike(searchParam, page);
        return users;
    }
    @Override
    public Page<UserRole> searchUsersByEmail(String email, int pageNumber, int pageSize) {
        String searchParam = "%" + email +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<UserRole> users = userRoleRepository.findByEmailLike(searchParam, page);
        return users;
    }
    @Override
    public boolean resetPassword(String userID) {
        log.info("resetPassword(userID={})", userID);
        User user = userRepository.findById(userID)
            .orElseThrow(()-> new NotFoundException("User " + userID + " does not found"));
        Boolean status = userRepository.updateUserPassword(
            user.getUserID(), passwordEncoder.encode(WebConstant.DEFUALT_PASSWORD));
        return status;
    }
    @Override
    public boolean assignUserRoles(UserInfo user) {
        log.info("assignUserRoles({})", user.toString());
        log.info("userroles {}", user.getRoles().size());
        // check input data 
        if(user.getRoles().size()>0){
            User fetchedUser = this.getUserByID(user.getUser().getUserID());
            List<Integer> roleIDs = user.getRoles().stream()
                .map(Role::getRoleID)
                .collect(Collectors.toList());
            
            List<Role> fetchedRoles = roleRepository.findAllById(roleIDs);
            // convert roleID to string
            List<String> roleIDsStr = fetchedRoles.stream()
                .map(role -> String.valueOf(role.getRoleID()))
                .collect(Collectors.toList());

            log.info("username: {}", fetchedUser.getUsername());
            log.info("listroleIDs: {}", String.join(",", roleIDsStr));
            // execute procedure 
            Boolean status = userRoleRepository.assignUserRoles(
                fetchedUser.getUsername(), 
                String.join(",", roleIDsStr));
            return status;
        }
        throw new NotFoundException("Does not found any roles for user " + user.getUser().getUserID());
    }
    @Override
    public UserInfo getUserAuthenticationInfo(String userID) {
        log.info("getUserAuthenticationInfo(userID={})", userID);
        User user = this.getUserByID(userID);
        // get and convert form userRoles to Role
        List<Role> roles = userRoleRepository.findByUserID(userID).stream().filter(userRole -> {
            String roleName = userRole.getRoleName();
            return roleName.equalsIgnoreCase(RoleConstant.ADMIN.name()) || 
            roleName.equalsIgnoreCase(RoleConstant.CUSTOMER.name()) ||
            roleName.equalsIgnoreCase(RoleConstant.EMPLOYEE.name());
        }).map(userRole -> new Role(userRole.getRoleID(), userRole.getRoleName()))
        .collect(Collectors.toList());
        // check if user has any roles
        if(roles==null || roles.size()==0){
            throw new NotFoundException("User " + userID + " does not have any roles");
        }

        return new UserInfo(user, roles);
    }
    @Override
    public User getUserByCustomerID(String customerID) {
        return userRepository.findByCustomerID(customerID)
            .orElseThrow(()-> new NotFoundException("User with Customer "+ customerID + " does not found"));
    }
    @Override
    public User getUserByEmployeeID(String employeeID) {
        return userRepository.findByEmployeeID(employeeID)
            .orElseThrow(()-> new NotFoundException("User with Employee "+ employeeID + " does not found"));
    }
}
