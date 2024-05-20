package com.phucx.gateway.model;

import org.springframework.security.oauth2.jwt.Jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Oauth2Jwt {
    private Jwt id_token;
    private Jwt access_token;
    private String refresh_token;
}
