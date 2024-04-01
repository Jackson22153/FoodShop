package com.phucx.account.filters;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.phucx.account.config.WebConfig;
import com.phucx.account.model.Customers;
import com.phucx.account.model.Employees;
import com.phucx.account.model.Users;
import com.phucx.account.service.customers.CustomersService;
import com.phucx.account.service.employees.EmployeesService;
import com.phucx.account.service.users.UsersService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
public class UserRegistrationFilter extends GenericFilter {
    @Autowired
    private UsersService usersService;
    @Autowired
    private CustomersService customersService;
    @Autowired
    private EmployeesService employeesService;

    private Logger logger = LoggerFactory.getLogger(UserRegistrationFilter.class);

    

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            var token = (Jwt)authentication.getPrincipal();
            if(token!=null){
                String userID = token.getSubject();
                Users user = usersService.getUserByID(userID);
                if(user!=null){
                    logger.info("username: " +user.getUsername());
                    String username = token.getClaimAsString(WebConfig.PREFERRED_USERNAME);
                    Users fetchedUser = usersService.getUserByID(userID);
                    boolean check = false;
                    if(fetchedUser!=null) check=true;
                    else{
                        String password = UUID.randomUUID().toString();
                        Users newUser = new Users(userID, username, password);
                        check = usersService.createUser(newUser);   
                    }
                    if(check){
                        boolean checkUser = false;

                        var authorities = authentication.getAuthorities();
                        GrantedAuthority customerRole = new SimpleGrantedAuthority("ROLE_CUSTOMER");
                        GrantedAuthority employeeRole = new SimpleGrantedAuthority("ROLE_EMPLOYEE");
                        // GrantedAuthority adminRole = new SimpleGrantedAuthority("ROLE_ADMIN");
                        if(authorities.contains(customerRole)){
                            Customers customer = new Customers();
                            customer.setCustomerID(userID);
                            customer.setContactName(username);
                            checkUser = customersService.createCustomer(customer);
                        }else if(authorities.contains(employeeRole)){
                            Employees employee = new Employees();
                            employee.setEmployeeID(userID);
                            employee.setFirstName(username);
                            employee.setLastName(username);
                            checkUser= employeesService.createEmployee(employee);
                        }
                        if(checkUser){
                            logger.info(username + " has been created");
                        }else{
                            logger.info(username + " does not have any roles");
                        }
                    }else{
                        logger.info(username + " can not be created");
                    }
                }else{
                    logger.info("user can not be created");
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
}
