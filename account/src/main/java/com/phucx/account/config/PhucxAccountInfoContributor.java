package com.phucx.account.config;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.info.InfoContributor;

@Configuration
public class PhucxAccountInfoContributor implements InfoContributor {
    

    @Override
    public void contribute(Builder builder) {
        Map<String, String> accountInfo = new HashMap<>();
        accountInfo.put("Info", "Phucx");
        accountInfo.put("Email", "Trongphuc22153@gmail.com");
        accountInfo.put("Version", "1.0.0");
        accountInfo.put("Description", "Account service is used to process data relating to users");
        builder.withDetail("Account service info", accountInfo);
    }
    
}
