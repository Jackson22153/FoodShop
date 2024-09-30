package com.phucx.account.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.model.UserAuthentication;
import com.phucx.account.service.phone.PhoneVerificationService;
import com.phucx.account.service.user.UserProfileService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private PhoneVerificationService phoneVerificationService;

    @Operation(summary = "Get user information", 
        tags = {"get", "tutorials", "public"},
        description = "Get username, roles of a user")
    @GetMapping("/userInfo")
    public ResponseEntity<UserAuthentication> getUserInfo(Authentication authentication) throws UserNotFoundException{
        UserAuthentication user = userProfileService.getUserAuthentication(authentication);
        log.info("UserAuthentication: {}", user);
        return ResponseEntity.ok().body(user);
    }

    @Operation(summary = "Generate OTP code for user's phone number", 
        tags = {"post", "private"}, 
        description = "Generate OTP code for user's phone number")
    @PostMapping("/phone/generateOTP")
    public ResponseEntity<Map<String, String>> generateOTP(@RequestParam(name = "phone") String phone) {
        String vnphone = "+" + phone.substring(1);
        String status = phoneVerificationService.generateOTP(vnphone);
        Map<String, String> result = new HashMap<>();
        result.put("status", status);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Verify user's phone number", 
        tags = {"post", "private"}, 
        description = "Verify user's phone number")
    @PostMapping("/phone/verifyOTP")
    public ResponseEntity<ResponseFormat> verifyUserOTP(
        @RequestParam(name = "otp") String otp, 
        @RequestParam(name = "phone") String phone,
        Authentication authentication) throws Exception {
        
        String vnphone = "+" + phone.substring(1);
        ResponseFormat responseFormat = phoneVerificationService.verifyOTP(otp, vnphone, authentication.getName());
        return ResponseEntity.ok().body(responseFormat);
    }
}
