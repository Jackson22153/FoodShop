package com.phucx.gateway.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

@Configuration
@EnableWebFluxSecurity
public class WebConfig {
    @Autowired
    private KeycloakLogoutHandler keycloakLogoutHandler;
    @Bean
    public SecurityWebFilterChain dFilterChain(ServerHttpSecurity http){
        http.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(ServerWebExchange exchange) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                return configuration;
            }
        }));

        http.csrf(csrf -> csrf.disable());
        // http.logout(logout -> logout.logoutHandler(null))
        http.authorizeExchange(request -> request
            // .pathMatchers("/home").permitAll()
            .pathMatchers("/shop/search/**").permitAll()
            .pathMatchers("/shop/home/**").permitAll()
            .pathMatchers("/isAuthenticated").permitAll()
            .pathMatchers("/account/**").permitAll()
            .anyExchange().authenticated());

        http.oauth2Login(Customizer.withDefaults())
            .logout(logout-> logout.logoutHandler(keycloakLogoutHandler));

        http.formLogin(login -> login.disable());
        http.httpBasic(login -> login.disable());
        return http.build();
    }

    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }
}
