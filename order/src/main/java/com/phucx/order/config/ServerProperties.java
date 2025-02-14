package com.phucx.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "phucx")
public class ServerProperties {
    private String serverDevUrl;
    private String paymentBaseUrl;
}
