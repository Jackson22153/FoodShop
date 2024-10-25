package com.phucx.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    @Bean
    public SecurityFilterChain dFilterChain(HttpSecurity https) throws Exception{
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new RoleConverter());
        // cors
        https.cors(Customizer.withDefaults());
        // csrf
        https.csrf(csrf -> csrf.disable());
        // session
        https.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // request
        https.authorizeHttpRequests(request -> request
            .requestMatchers("/document/**", "/swagger-ui/**", "/v3/**").permitAll()
            .requestMatchers("/paypal/pay/cancel", "paypal/pay/successful").permitAll()
            .requestMatchers("/zalopay/pay/payment", "/zalopay/callback").permitAll()
            .requestMatchers("/pay/**", "/methods/**", "/shipping/**").authenticated()
            .requestMatchers("/paypal/**", "/cod/**", "/pay/**", "/zalopay/**").hasRole("CUSTOMER")
            .requestMatchers("/invoice/**").hasRole("CUSTOMER")
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/address/**").authenticated()
            .anyRequest().denyAll());
        // oauth2 resource
        https.oauth2ResourceServer(resource -> resource.jwt(jwt -> jwt
            .jwtAuthenticationConverter(jwtAuthenticationConverter)));
        return https.build();
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
