package com.phucx.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.account.constant.WebConstant;
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
    // customers
    // @Operation(summary = "Add new customer", 
    //     tags = {"put", "tutorials", "admin"})
    // @PutMapping("/customers")
    // public ResponseEntity<ResponseFormat> addCustomer(
    //     @RequestBody CustomerAccount customerAccount
    // ) throws InvalidUserException{
    //     Boolean status = customerService.addNewCustomer(customerAccount);
    //     return ResponseEntity.ok().body(new ResponseFormat(status));
    // }

    @Operation(summary = "Get customers", 
        tags = {"get", "tutorials", "admin"})
    @GetMapping("/customers")
    public ResponseEntity<Page<CustomerDetail>> getCustomers(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "customerID", required = false) String customerID,
        @RequestParam(name = "contactName", required = false) String contactName,
        @RequestParam(name = "username", required = false) String username,
        @RequestParam(name = "email", required = false) String email
    ){        
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<CustomerDetail> customers = null;
        if(customerID!=null) customers = customerService.searchCustomersByCustomerID(customerID, pageNumber, WebConstant.PAGE_SIZE);
        else if(contactName!=null) customers = customerService.searchCustomersByContactName(contactName, pageNumber, WebConstant.PAGE_SIZE);
        else if(username!=null) customers = customerService.searchCustomersByUsername(username, pageNumber, WebConstant.PAGE_SIZE);
        else if(email!=null) customers = customerService.searchCustomersByEmail(email, pageNumber, WebConstant.PAGE_SIZE);
        else customers = customerService.getCustomers(pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(customers);
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
    // employees
    // @Operation(summary = "Add new employee", 
    //     tags = {"put", "tutorials", "admin"})
    // @PutMapping("/employees")
    // public ResponseEntity<ResponseFormat> addEmployee(
    //     @RequestBody EmployeeAccount employeeAccount
    // ) throws InvalidUserException{
    //     Boolean status = employeeService.addNewEmployee(employeeAccount);
    //     return ResponseEntity.ok().body(new ResponseFormat(status));
    // }

    @Operation(summary = "Get employees", 
        tags = {"get", "tutorials", "admin"})
    @GetMapping("/employees")
    public ResponseEntity<Page<EmployeeDetail>> getEmployees(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "employeeID", required = false) String employeeID,
        @RequestParam(name = "firstName", required = false) String firstName,
        @RequestParam(name = "lastName", required = false) String lastName,
        @RequestParam(name = "username", required = false) String username,
        @RequestParam(name = "email", required = false) String email
    ){        
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<EmployeeDetail> employees = null;
        if(employeeID!=null) employees = employeeService.searchEmployeesByEmployeeID(employeeID, pageNumber, WebConstant.PAGE_SIZE);
        else if(firstName!=null) employees = employeeService.searchEmployeesByFirstName(firstName, pageNumber, WebConstant.PAGE_SIZE);
        else if(lastName!=null) employees = employeeService.searchEmployeesByLastName(lastName, pageNumber, WebConstant.PAGE_SIZE);
        else if(username!=null) employees = employeeService.searchEmployeesByUsername(username, pageNumber, WebConstant.PAGE_SIZE);
        else if(email!=null) employees = employeeService.searchEmployeesByEmail(email, pageNumber, WebConstant.PAGE_SIZE);
        else employees = employeeService.getEmployees(pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(employees);
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

    // @Operation(summary = "Reset password for a user", 
    //     tags = {"post", "tutorials", "admin"})
    // @PostMapping("/users/{userID}/password")
    // public ResponseEntity<ResponseFormat> resetPassword(
    //     @PathVariable(name = "userID") String userID
    // ) throws UserNotFoundException{
    //     Boolean status = userProfileService.resetPassword(userID);
    //     return ResponseEntity.ok().body(new ResponseFormat(status));
    // }

    // @Operation(summary = "Get roles", 
    //     tags = {"post", "tutorials", "admin"})
    // @PostMapping("/users/roles")
    // public ResponseEntity<ResponseFormat> assignRoles(
    //     @RequestBody UserInfo user
    // ) throws UserNotFoundException{
    //     Boolean status = userProfileService.assignUserRoles(user);
    //     return ResponseEntity.ok().body(new ResponseFormat(status));
    // }
    // roles
}
