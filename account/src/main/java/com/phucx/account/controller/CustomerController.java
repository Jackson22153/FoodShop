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

import com.phucx.account.exception.CustomerNotFoundException;
import com.phucx.account.exception.InvalidUserException;
import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.ImageFormat;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.customer.CustomerService;
import com.phucx.account.service.image.CustomerImageService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerImageService customerImageService;

    @Operation(summary = "Check user's role", 
        tags = {"get", "tutorials", "customer"})
    @GetMapping("/isCustomer")
    public ResponseEntity<ResponseFormat> isCustomer(){
        return ResponseEntity.ok().body(new ResponseFormat(true));
    }
    // GET CUSTOMER'S INFOMATION
    @Operation(summary = "Get customer information", 
        tags = {"get", "tutorials", "customer"})
    @GetMapping("/info")
    public ResponseEntity<CustomerDetail> getUserInfo(Authentication authentication
    ) throws UserNotFoundException, InvalidUserException{
        String userID = authentication.getName();
        CustomerDetail customer = customerService.getCustomerDetail(userID);
        return ResponseEntity.ok().body(customer);
    }
    // UPDATE CUSTOMER'S INFOMATION
    @Operation(summary = "Update customer information", 
        tags = {"post", "tutorials", "customer"})
    @PostMapping("/info")
    public ResponseEntity<ResponseFormat> updateUserInfo(
        Authentication authentication,
        @RequestBody CustomerDetail customer
    ) throws CustomerNotFoundException{
        CustomerDetail updatedCustomer = customerService.updateCustomerInfo(customer);
        return ResponseEntity.ok().body(new ResponseFormat(updatedCustomer!=null?true: false));
    }

    // set image
    @Operation(summary = "Upload customer's image", 
        tags = {"post", "tutorials", "customer"})
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageFormat> uploadCustomerImage(
        @RequestBody MultipartFile file,
        @RequestHeader(name = "X-Forwarded-Uri", required = false) String requestUri,
        @RequestHeader(name = "X-Server-Port", required = false) Integer serverPort
    ) throws IOException {
        String filename = customerImageService.uploadCustomerImage(file);
        String imageUrl = customerImageService.getCurrentUrl(requestUri, serverPort) + "/" + filename;
        ImageFormat imageFormat = new ImageFormat(imageUrl);
        return ResponseEntity.ok().body(imageFormat);
    }
}
