package com.phucx.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.customer.CustomerService;
import com.phucx.account.service.employee.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;

    @Operation(summary = "Check user role", 
        tags = {"get", "tutorials", "admin"},
        description = "Check whether a user is admin or not")
    @GetMapping("/isAdmin")
    public ResponseEntity<ResponseFormat> isAdmin(){
        ResponseFormat format = new ResponseFormat();
        format.setStatus(true);
        return ResponseEntity.ok().body(format);
    }

    @Operation(summary = "Get user by CustomerID", 
        tags = {"get", "tutorials", "admin"})
    @GetMapping("/customers/{customerID}")
    public ResponseEntity<CustomerDetail> getUserByCustomerID(
        @PathVariable(name = "customerID") String customerID
    ) throws UserNotFoundException{
        CustomerDetail customer = customerService.getCustomerByID(customerID);
        return ResponseEntity.ok().body(customer);
    }

    @Operation(summary = "Get employee by EmployeeID", 
        tags = {"get", "tutorials", "admin"})
    @GetMapping("/employees/{employeeID}")
    public ResponseEntity<EmployeeDetail> getEmployeeDetail(
        @PathVariable(name = "employeeID") String employeeID
    ) throws UserNotFoundException{
        EmployeeDetail employee = employeeService.getEmployee(employeeID);
        return ResponseEntity.ok().body(employee);
    }
    
    @Operation(summary = "Update employee information", 
        tags = {"post", "tutorials", "admin"})
    @PostMapping("/employees")
    public ResponseEntity<ResponseFormat> updateEmployeeDetail(
        @RequestBody EmployeeDetail employee
    ) throws JsonProcessingException, EmployeeNotFoundException{
        EmployeeDetail updatedEmployee = employeeService.updateAdminEmployeeInfo(employee);
        Boolean status = updatedEmployee!=null?true:false;
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }
}
