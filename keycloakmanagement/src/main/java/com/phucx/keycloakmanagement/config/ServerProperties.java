package com.phucx.keycloakmanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Configuration
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "phucx")
public class ServerProperties {
    private String serverUrl;
}
