package com.phucx.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.employee.EmployeeService;
import com.phucx.account.service.user.UserService;

@RestController
@RequestMapping("employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UserService userService;


    @GetMapping("/isEmployee")
    public ResponseEntity<ResponseFormat> isEmployee(){
        return ResponseEntity.ok().body(new ResponseFormat(true));
    }
    // GET EMPLOYEE'S INFORMATION
    @GetMapping("/info")
    public ResponseEntity<EmployeeDetail> getUserInfo(Authentication authentication){
        String username = userService.getUsername(authentication);
        EmployeeDetail employee = employeeService.getEmployeeDetail(username);
        return ResponseEntity.ok().body(employee);
    }
    // UPDATE EMPLOYEE'S INFORMATION
    @PostMapping("/info")
    public ResponseEntity<ResponseFormat> updateUserInfo(
        @RequestBody EmployeeDetail employee
    ){
        Boolean status = employeeService.updateEmployeeInfo(employee);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }
}
