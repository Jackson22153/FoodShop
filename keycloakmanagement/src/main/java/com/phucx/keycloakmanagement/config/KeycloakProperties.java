package com.phucx.keycloakmanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String adminUsername;
    private String adminPassword;
    private String serverUrl;
    private String realm;
    private String clientId;

}
