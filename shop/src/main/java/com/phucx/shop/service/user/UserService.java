package com.phucx.shop.service.user;

import org.springframework.security.core.Authentication;

public interface UserService {
    public String getUsername(Authentication authentication);
    public String getUserID(Authentication authentication);
}
