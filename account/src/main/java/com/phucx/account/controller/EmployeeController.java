package com.phucx.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.model.Employees;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.employees.EmployeesService;

@RestController
@RequestMapping("employee")
public class EmployeeController {
    @Autowired
    private EmployeesService employeesService;


    @GetMapping("info")
    public ResponseEntity<Employees> getUserInfo(Authentication authentication){
        String userID = authentication.getName();
        Employees employee = employeesService.getEmployeeDetail(userID);
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("info")
    public ResponseEntity<ResponseFormat> updateUserInfo(
        Authentication authentication,
        @RequestBody Employees employee
    ){
        // String userID = authentication.getName();
        // customer.setCustomerID(userID);
        boolean check = employeesService.updateEmployeeInfo(employee);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }
}
