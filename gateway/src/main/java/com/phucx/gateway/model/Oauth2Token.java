package com.phucx.gateway.model;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class Oauth2Token {
    private String access_token;
    private String refresh_token;
    private String id_token;
    
}
