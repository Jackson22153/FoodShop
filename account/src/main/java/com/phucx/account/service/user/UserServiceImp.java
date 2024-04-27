package com.phucx.account.service.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.phucx.account.config.WebConfig;
import com.phucx.account.model.CustomerAccounts;
import com.phucx.account.model.EmployeeAccounts;
import com.phucx.account.model.Roles;
import com.phucx.account.model.UserRoles;
import com.phucx.account.model.UserRolesUtils;
import com.phucx.account.model.User;
import com.phucx.account.repository.CustomerAccountsRepository;
import com.phucx.account.repository.EmployeeAccountsRepository;
import com.phucx.account.repository.UserRolesRepository;
import com.phucx.account.repository.UserRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRolesRepository userRolesRepository;
    @Autowired
    private CustomerAccountsRepository customerAccountsRepository;
    @Autowired
    private EmployeeAccountsRepository employeeAccountsRepository;
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
    public UserRolesUtils getUserRoles(String userID) {
        List<UserRoles> userRoles = userRolesRepository.findByUserID(userID);
        // List<UserRoles> userRoles = userRolesService.getUserRoles(userID);
        if(userRoles.size()>0){
            UserRoles firstEntity = userRoles.get(0);
            User user = new User(firstEntity.getUserID(), firstEntity.getUsername(), null);
            List<Roles> roles = new ArrayList<>();
            for (UserRoles userRole : userRoles) {
                roles.add(new Roles(null, userRole.getRoleName()));
            }
            return new UserRolesUtils(user, roles);
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
        CustomerAccounts customerAccounts = customerAccountsRepository.findByCustomerID(customerID)
            .orElseThrow(()-> new NotFoundException("Customer "+customerID+" does not found"));
        return customerAccounts.getUserID();
    }
    @Override
    public String getUserIdOfEmployeeID(String employeeID) {
        EmployeeAccounts employeeAccounts = employeeAccountsRepository.findByEmployeeID(employeeID)
            .orElseThrow(()-> new NotFoundException("Employee "+ employeeID +" does not found"));
        return employeeAccounts.getEmployeeID();
    }
}
