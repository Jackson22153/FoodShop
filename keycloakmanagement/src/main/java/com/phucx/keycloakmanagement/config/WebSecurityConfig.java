package com.phucx.keycloakmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    @Bean
    public SecurityFilterChain dFilterChain(HttpSecurity https) throws Exception{

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new RoleConverter());

        https.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        https.cors(Customizer.withDefaults());
        https.csrf(csrf -> csrf.disable());
        https.authorizeHttpRequests(request -> request
            .requestMatchers("/keycloak/**").hasRole("ADMIN")
            .requestMatchers("/document/**", "/v3/**", "/swagger-ui/**").permitAll() 
            .requestMatchers("/actuator/**").permitAll()
            .anyRequest().denyAll());
        https.oauth2ResourceServer(resource -> resource
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        return https.build();
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
