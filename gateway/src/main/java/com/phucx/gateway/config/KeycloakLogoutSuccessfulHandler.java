package com.phucx.gateway.config;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.util.MultiValueMap;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class KeycloakLogoutSuccessfulHandler implements ServerLogoutSuccessHandler {
    private final String SESSION_COOKIE = "SESSION";

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        return Mono.fromRunnable(()->{
            MultiValueMap<String, HttpCookie> cookies = exchange.getExchange().getRequest().getCookies();
            if(cookies.containsKey(SESSION_COOKIE)){
                ResponseCookie removedCookie = ResponseCookie.from(SESSION_COOKIE, null)
                    .maxAge(0)
                    .path("/")
                    .build();
    
                exchange.getExchange().getResponse().addCookie(removedCookie);
            }
        });
    }
    
}
