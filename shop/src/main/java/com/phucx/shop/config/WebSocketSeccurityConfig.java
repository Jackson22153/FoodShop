package com.phucx.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSeccurityConfig {
    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder builder){
        builder
            .anyMessage().permitAll();
            // .nullDestMatcher().authenticated()
            // .simpDestMatchers("/app/placeOrder").hasRole("CUSTOMER")
            // .simpDestMatchers("/app/order.validate").hasRole("EMPLOYEE")
            // .simpSubscribeDestMatchers("/user/**").hasRole("CUSTOMER");
            // .simpSubscribeDestMatchers("/topic/order/**").hasRole("EMPLOYEE")
            // .anyMessage().denyAll();
        return builder.build();
    }
}
