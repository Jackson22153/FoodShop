package com.phucx.account.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@ComponentScans(value = {
    @ComponentScan("com.phucx.account.config"),
    @ComponentScan("com.phucx.account.filters"),
    @ComponentScan("com.phucx.account.eventListener")
})
public class WebConfig {
    // token
    public final static String PREFERRED_USERNAME="preferred_username";
    public final static String REALM_ACCESS_CLAIM="realm_access";
    public final static String ROLES_CLAIM="roles";
    // roles
    public final static String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public final static String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";
    public final static String ROLE_ADMIN = "ROLE_ADMIN";

    // status notification
    public final static String FAILED_NOTIFICATION="faile";
    public final static String SUCCESSFUL_NOTIFICATION="success";

    @Bean
    public SecurityFilterChain dFilterChain(HttpSecurity http) throws Exception{
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new RoleConverter());
        http.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.cors(Customizer.withDefaults());
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/chat/**"));
        http.headers(header -> header.frameOptions(frame -> frame.sameOrigin()));
        http.authorizeHttpRequests(request -> request
            .requestMatchers("/admin/*").permitAll()
            .requestMatchers("/customer/*").hasRole("CUSTOMER")
            .requestMatchers("/employee/*").hasRole("EMPLOYEE")
            .anyRequest().authenticated());
        http.oauth2ResourceServer(resource -> resource.jwt(jwt -> jwt
            .jwtAuthenticationConverter(jwtAuthenticationConverter)));
        return http.build();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
