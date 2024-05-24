package com.phucx.account.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(0)
@Component
public class JwtFilter extends OncePerRequestFilter{

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwtSetUri;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Request path: {}", request.getRequestURI());
        String token = request.getHeader("Authorization");
        if(token!=null){
            token = token.substring(7);
            Jwt jwt = NimbusJwtDecoder.withJwkSetUri(jwtSetUri).build().decode(token);
            log.info("Token ${}", jwt.getSubject());
        }
        // var headers = request.getHeaderNames();
        filterChain.doFilter(request, response);
    }
    
}
