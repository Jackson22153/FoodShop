package com.phucx.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
@EnableAspectJAutoProxy
@ComponentScans({
    @ComponentScan("com.phucx.notification.aspects"),
    @ComponentScan("com.phucx.notification.eventListenter")
})
public class WebSecurityConfig {
    // token
    public final static String PREFERRED_USERNAME="preferred_username";
    public final static String REALM_ACCESS_CLAIM="realm_access";
    public final static String ROLES_CLAIM="roles";
    // roles
    public final static String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public final static String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";
    public final static String ROLE_ADMIN = "ROLE_ADMIN";


    @Bean
    public SecurityFilterChain dFilterChain(HttpSecurity http) throws Exception{
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new RoleConverter());
        http.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // http.addFilterBefore(new JwtFilter(), BearerTokenAuthenticationFilter.class);
        http.cors(Customizer.withDefaults());
        // http.csrf(csrf -> csrf.ignoringRequestMatchers("/chat/**"));
        http.csrf(csrf-> csrf.disable());
        http.headers(header -> header.frameOptions(frame -> frame.sameOrigin()));
        http.authorizeHttpRequests(request -> request
            .requestMatchers("/customer/**").hasRole("CUSTOMER")
            .requestMatchers("/employee/**").hasRole("EMPLOYEE")
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/chat/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/**", "/document/**").permitAll()
            .anyRequest().authenticated());
        http.oauth2ResourceServer(resource -> resource.jwt(jwt -> jwt
            .jwtAuthenticationConverter(jwtAuthenticationConverter)));
        return http.build();
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
