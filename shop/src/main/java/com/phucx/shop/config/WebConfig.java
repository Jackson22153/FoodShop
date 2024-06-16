package com.phucx.shop.config;

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
    @ComponentScan("com.phucx.shop.aspects"),
    @ComponentScan("com.phucx.shop.messageQueueListener")
})
public class WebConfig {  
    public static int PAGE_SIZE = 12;
    public static int RECOMMENDED_PAGE_SIZE = 4;
    public static int RECOMMENDED_PRODUCT_PAGE_SIZE = 8;
    public final static String PREFERRED_USERNAME="preferred_username";

    public final static String ORDER_QUEUE = "order";
    public final static String ORDER_ROUTING_KEY = "order";

    @Bean
    public SecurityFilterChain dFilterChain(HttpSecurity http) throws Exception{
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new RoleConverter());
        http.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());
        http.authorizeHttpRequests(request -> request
            .requestMatchers("/home/**", "/image/**", "/search/**").permitAll()
            .requestMatchers("/discount/**", "/category/**", "/product/**", "/actuator/**").hasRole("ADMIN")
            .requestMatchers("/cart/**").hasRole("CUSTOMER")
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
