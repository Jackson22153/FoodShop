package com.phucx.account.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class CustomerController {
    @GetMapping("/info")
    public String userOIcd(@AuthenticationPrincipal OidcUser oidc){
        return oidc.getIdToken().getTokenValue();
    }
}
