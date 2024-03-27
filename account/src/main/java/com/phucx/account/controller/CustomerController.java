package com.phucx.account.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.model.Users;
import com.phucx.account.service.users.UsersService;

@RestController
@RequestMapping("customer")
public class CustomerController {
    @Autowired
    private UsersService usersService;
    @GetMapping("/info")
    public String userOIcd(Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return authentication.getName();
    }

    @GetMapping("{username}")
    public ResponseEntity<Users> getUserAccount(@PathVariable String username){
        Users user = usersService.getUser(username);
        return ResponseEntity.ok().body(user);
    }
}
