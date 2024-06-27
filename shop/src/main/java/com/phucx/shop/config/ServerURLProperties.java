package com.phucx.shop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "phucx")
public class ServerURLProperties {
    private String serverDevUrl;
}
