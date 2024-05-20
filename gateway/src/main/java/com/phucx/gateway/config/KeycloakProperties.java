package com.phucx.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@Getter @Setter @ToString
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String tokenUri;
    private String authorizationUri;
    private String issuer;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scope;
    private String jwkSetUri;
}
