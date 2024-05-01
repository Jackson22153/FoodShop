package com.phucx.account.service.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.phucx.account.config.WebConfig;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.Role;
import com.phucx.account.model.UserRole;
import com.phucx.account.model.UserRolesDTO;
import com.phucx.account.model.User;
import com.phucx.account.repository.CustomerAccountRepository;
import com.phucx.account.repository.EmployeeAccountRepository;
import com.phucx.account.repository.UserRoleRepository;
import com.phucx.account.repository.UserRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private CustomerAccountRepository customerAccountRepository;
    @Autowired
    private EmployeeAccountRepository employeeAccountRepository;
    @Override
    public User getUser(String username) {
        User user = userRepository.findByUsername(username);
        if(user!=null){
            return user;
        }
        return null;
    }
    @Override
    public User getUserByID(String userID) {
        var opUser = userRepository.findById(userID);
        if(opUser.isPresent())
            return opUser.get();
        else return null;
    }
    @Override
    public boolean createUser(User user) {
        try {
            String username = user.getUsername();
            String password = user.getPassword();
            User existedUser = this.getUser(username);
            if(existedUser==null){
                String hashedPassword = passwordEncoder.encode(password);
                User newUser = new User(user.getUserID(), username, hashedPassword);
                User check = userRepository.save(newUser);
                if(check!=null)
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public UserRolesDTO getUserRoles(String userID) {
        List<UserRole> userRoles = userRoleRepository.findByUserID(userID);
        // List<UserRole> userRole = userRoleService.getUserRole(userID);
        if(userRoles!=null && userRoles.size()>0){
            UserRole firstEntity = userRoles.get(0);
            User user = new User(firstEntity.getUserID(), firstEntity.getUsername(), null);
            List<Role> roles = new ArrayList<>();
            for (UserRole userRole : userRoles) {
                roles.add(new Role(null, userRole.getRoleName()));
            }
            return new UserRolesDTO(user, roles);
        }
        return null;
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
        return employeeAccount.getEmployeeID();
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
}
