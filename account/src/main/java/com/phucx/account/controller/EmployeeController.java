package com.phucx.account.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.ImageFormat;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.employee.EmployeeService;
import com.phucx.account.service.image.EmployeeImageService;
import com.phucx.account.service.user.UserService;

@RestController
@RequestMapping("employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeImageService employeeImageService;


    @GetMapping("/isEmployee")
    public ResponseEntity<ResponseFormat> isEmployee(){
        return ResponseEntity.ok().body(new ResponseFormat(true));
    }
    // GET EMPLOYEE'S INFORMATION
    @GetMapping("/info")
    public ResponseEntity<EmployeeDetail> getUserInfo(Authentication authentication) throws UserNotFoundException{
        String username = userService.getUsername(authentication);
        EmployeeDetail employee = employeeService.getEmployeeDetail(username);
        return ResponseEntity.ok().body(employee);
    }
    // UPDATE EMPLOYEE'S INFORMATION
    @PostMapping("/info")
    public ResponseEntity<ResponseFormat> updateUserInfo(
        @RequestBody EmployeeDetail employee
    ) throws EmployeeNotFoundException{
        Boolean status = employeeService.updateEmployeeInfo(employee);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    // set image
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageFormat> uploadEmployeeImage(
        @RequestBody MultipartFile file,
        @RequestHeader(name = "X-Forwarded-Uri", required = false) String requestUri,
        @RequestHeader(name = "X-Server-Port", required = false) Integer serverPort
    ) throws IOException {

        String filename = employeeImageService.uploadEmployeeImage(file);
        String imageUrl = employeeImageService.getCurrentUrl(requestUri, serverPort) + "/" + filename;
        ImageFormat imageFormat = new ImageFormat(imageUrl);
        return ResponseEntity.ok().body(imageFormat);
    }
}
