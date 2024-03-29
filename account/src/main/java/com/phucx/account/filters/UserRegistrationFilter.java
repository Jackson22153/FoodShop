package com.phucx.account.filters;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.phucx.account.config.WebConfig;
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
                    System.out.println("username: " +user.getUsername());
                }else{
                    String username = token.getClaimAsString(WebConfig.PREFERRED_USERNAME);
                    String password = UUID.randomUUID().toString();
                    Users newUser = new Users(userID, username, password);
                    boolean check = usersService.createUser(newUser);
                    if(check){
                        System.out.println(username + " has been created");
                        boolean checkUser = false;
                        var authorities = authentication.getAuthorities();
                        GrantedAuthority customerRole = new SimpleGrantedAuthority("ROLE_CUSTOMER");
                        GrantedAuthority employeeRole = new SimpleGrantedAuthority("ROLE_EMPLOYEE");
                        // GrantedAuthority adminRole = new SimpleGrantedAuthority("ROLE_ADMIN");
                        if(authorities.contains(customerRole)){
                            checkUser = customersService.createCustomer(userID);
                        }else if(authorities.contains(employeeRole)){
                            checkUser= employeesService.createEmployee(userID);
                        }
                        if(checkUser){
                            System.out.println(username + " has been created");
                        }
                    }else{
                        System.out.println(username + " can not be created");
                    }
    
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
}
