package com.phucx.keycloakmanagement.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.keycloakmanagement.constant.WebConstant;
import com.phucx.keycloakmanagement.exception.NotFoundException;
import com.phucx.keycloakmanagement.model.CustomerDetails;
import com.phucx.keycloakmanagement.model.EmployeeDetails;
import com.phucx.keycloakmanagement.model.User;
import com.phucx.keycloakmanagement.service.customer.CustomerService;
import com.phucx.keycloakmanagement.service.employee.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/keycloak")
public class KeycloakController {   
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;

    @Operation(summary = "Get customers from authorization server", tags = {"get", "admin", "tutorial"})
    @GetMapping("/customers")
    public ResponseEntity<List<User>> getCustomerUsers(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "firstName", required = false) String firstName,
        @RequestParam(name = "lastName", required = false) String lastName,
        @RequestParam(name = "username", required = false) String username,
        @RequestParam(name = "email", required = false) String email
    ) throws JsonProcessingException{
        List<User> customers = new ArrayList<>();
        pageNumber = pageNumber!=null?pageNumber:0;
        if(firstName!=null) customers = customerService.searchCustomersByFirstName(firstName, pageNumber, WebConstant.PAGE_SIZE);
        else if(lastName!=null) customers = customerService.searchCustomersByLastName(lastName, pageNumber, WebConstant.PAGE_SIZE);
        else if(username!=null) customers = customerService.searchCustomersByUsername(username, pageNumber, WebConstant.PAGE_SIZE);
        else if(email!=null) customers = customerService.searchCustomersByEmail(email, pageNumber, WebConstant.PAGE_SIZE);
        else customers = customerService.getCustomers(pageNumber, WebConstant.PAGE_SIZE);
     
        return ResponseEntity.ok().body(customers);
    }

    @Operation(summary = "Get customer details", tags = {"get", "admin", "tutorial"})
    @GetMapping("/customers/{userID}")
    public ResponseEntity<CustomerDetails> getCustomerByuserID(
        @PathVariable(name = "userID") String userID
    ) throws NotFoundException, JsonProcessingException{
        CustomerDetails customer = customerService.getCustomerDetails(userID);
        return ResponseEntity.ok().body(customer);
    }

    @Operation(summary = "Get employees", tags = {"get", "admin", "tutorial"})
    @GetMapping("/employees")
    public ResponseEntity<List<User>> getEmployeeUsers(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "firstName", required = false) String firstName,
        @RequestParam(name = "lastName", required = false) String lastName,
        @RequestParam(name = "username", required = false) String username,
        @RequestParam(name = "email", required = false) String email
    ) throws JsonProcessingException{
        pageNumber = pageNumber!=null?pageNumber:0;
        List<User> employees = new ArrayList<>();
        if(firstName!=null) employees = employeeService.searchEmployeesByFirstName(firstName, pageNumber, WebConstant.PAGE_SIZE);
        else if(lastName!=null) employees = employeeService.searchEmployeesByLastName(lastName, pageNumber, WebConstant.PAGE_SIZE);
        else if(username!=null) employees = employeeService.searchEmployeesByUsername(username, pageNumber, WebConstant.PAGE_SIZE);
        else if(email!=null) employees = employeeService.searchEmployeesByEmail(email, pageNumber, WebConstant.PAGE_SIZE);
        else employees = employeeService.getEmployees(pageNumber, WebConstant.PAGE_SIZE);

        return ResponseEntity.ok().body(employees);
    }

    @Operation(summary = "Get employee details", tags = {"get", "admin", "tutorial"})
    @GetMapping("/employees/{userID}")
    public ResponseEntity<EmployeeDetails> getEmployeeByUserID(
        @PathVariable(name = "userID") String userID
    ) throws NotFoundException, JsonProcessingException{
        EmployeeDetails employee = employeeService.getEmployeeDetails(userID);
        return ResponseEntity.ok().body(employee);
    }
}
