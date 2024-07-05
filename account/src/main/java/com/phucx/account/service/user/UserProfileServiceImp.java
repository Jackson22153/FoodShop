package com.phucx.account.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.phucx.account.config.WebConfig;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.exception.CustomerNotFoundException;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.UserAuthentication;
import com.phucx.account.model.UserInfo;
import com.phucx.account.model.UserProfile;
import com.phucx.account.repository.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserProfileServiceImp implements UserProfileService {
    @Autowired
    private  UserProfileRepository userProfileRepository;

    @Override
    public UserProfile getUserProfile(String userID) throws UserNotFoundException {
        log.info("getUserProfile(userID={})", userID);
        UserProfile user = userProfileRepository.findByUserID(userID)
            .orElseThrow(()-> new UserNotFoundException("User profile with userId " + userID + " does not found"));
        return user;
    }
    @Override
    public UserProfile getUserProfileByID(String userProfileID) throws UserNotFoundException {
        log.info("getUserProfileByID(userProfileID={})", userProfileID);
        UserProfile user = userProfileRepository.findById(userProfileID)
            .orElseThrow(()-> new UserNotFoundException("User profile " + userProfileID + " does not found"));
        return user;
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
    public UserProfile getUserProfileByCustomerID(String customerID) throws CustomerNotFoundException {
        log.info("getUserProfileByCustomerID({})", customerID);
        return userProfileRepository.findByCustomerID(customerID)
            .orElseThrow(()-> new CustomerNotFoundException("Customer "+customerID+" does not found"));
    }
    @Override
    public UserProfile getUserProfileByEmployeeID(String employeeID) throws EmployeeNotFoundException {
        log.info("getUserProfileByEmployeeID({})", employeeID);
        return userProfileRepository.findByEmployeeID(employeeID)
            .orElseThrow(()-> new EmployeeNotFoundException("Employee "+ employeeID + " does not found"));
    }
    @Override
    public UserAuthentication getUserAuthentication(Authentication authentication) {
        log.info("getUserAuthentication({})", authentication);

        Jwt jwt = (Jwt) authentication.getPrincipal();
        // extract username
        String username = jwt.getClaimAsString(WebConstant.PREFERRED_USERNAME);
        // extract email
        String email = jwt.getClaimAsString(WebConstant.EMAIL);
        // userid
        String userID = jwt.getSubject();
        // extract roles
        List<String> roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(role -> role.substring(WebConstant.ROLE_PREFIX.length()))
            .collect(Collectors.toList());

        UserInfo userInfo = new UserInfo(userID, username, email);
        UserAuthentication userAuthentication = new UserAuthentication(userInfo, roles);
        return userAuthentication;
    }
}
