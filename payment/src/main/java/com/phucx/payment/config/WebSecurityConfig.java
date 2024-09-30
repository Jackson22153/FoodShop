package com.phucx.payment.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    @Bean
    public SecurityFilterChain dFilterChain(HttpSecurity https) throws Exception{
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new RoleConverter());
        // cors
        https.cors(Customizer.withDefaults());
        // https.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {

        //     @Override
        //     @Nullable
        //     public CorsConfiguration getCorsConfiguration(HttpServletRequest arg0) {
        //         CorsConfiguration configuration = new CorsConfiguration();
        //         configuration.setAllowCredentials(true);
        //         configuration.setAllowedMethods(Collections.singletonList("*"));
        //         configuration.setAllowedHeaders(Collections.singletonList("*"));
        //         configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        //         return configuration;
        //     }
        // }));
        // csrf
        https.csrf(csrf -> csrf.disable());
        // session
        https.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // request
        https.authorizeHttpRequests(request -> request
            .requestMatchers("/document/**", "/swagger-ui/**", "/v3/**").permitAll()
            .requestMatchers("/paypal/pay/cancel", "paypal/pay/successful").permitAll()
            .requestMatchers("/momo/pay/cancel", "momo/pay/successful").permitAll()
            .requestMatchers("zalopay/pay/successful", "zalopay/callback").permitAll()
            .requestMatchers("/pay/**", "/methods/**").authenticated()
            .requestMatchers("/paypal/**", "/cod/**", "/pay/**", "/momo/**", "/zalopay/**").hasRole("CUSTOMER")
            .requestMatchers("/invoice/**").hasRole("CUSTOMER")
            .anyRequest().denyAll());
            // .anyRequest().permitAll());
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
}
