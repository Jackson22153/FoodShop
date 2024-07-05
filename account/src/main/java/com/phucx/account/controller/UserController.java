package com.phucx.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.UserAuthentication;
import com.phucx.account.service.user.UserProfileService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserProfileService userProfileService;

    @Operation(summary = "Get user information", 
        tags = {"get", "tutorials", "public"},
        description = "Get username, roles of a user")
    @GetMapping("/userInfo")
    public ResponseEntity<UserAuthentication> getUserInfo(Authentication authentication) throws UserNotFoundException{
        UserAuthentication user = userProfileService.getUserAuthentication(authentication);
        log.info("UserAuthentication: {}", user);
        return ResponseEntity.ok().body(user);
    }
}
