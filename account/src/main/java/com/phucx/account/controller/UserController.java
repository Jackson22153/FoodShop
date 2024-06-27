package com.phucx.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Get user information", 
        tags = {"get", "tutorials", "public"},
        description = "Get username, roles of a user")
    @GetMapping("/userInfo")
    public ResponseEntity<UserInfo> getUserInfo(Authentication authentication) throws UserNotFoundException{
        UserInfo user = new UserInfo();
        user.setUser(new User());
        if(authentication!=null){
            user = userService.getUserInfo(authentication.getName());
        }
        return ResponseEntity.ok().body(user);
    }
}
