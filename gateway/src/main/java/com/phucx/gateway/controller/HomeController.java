package com.phucx.gateway.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.phucx.gateway.constant.GatewayConstant;

import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class HomeController {
    private ServerRedirectStrategy redirectStrategy;
    //  = new DefaultServerRedirectStrategy();


    @GetMapping("/home")
    public Mono<String> home(){
        return Mono.just("home");
    }

    @GetMapping("/loginBE")
    public Mono<Void> login(ServerWebExchange exchange){
        redirectStrategy = new DefaultServerRedirectStrategy();
        return redirectStrategy.sendRedirect(exchange, URI.create(GatewayConstant.FoodShopUrl));
    }

    @PostMapping("isAuthenticated")
    public Mono<Map<String, Boolean>> checkAuthentication(Authentication authentication){
        Map<String, Boolean> result = new HashMap<>();
        if(authentication!=null)
            result.put("isAuthenticated", authentication.isAuthenticated());
        else result.put("isAuthenticated", false);
        return Mono.just(result);
    }

    @GetMapping("user")
    public Mono<String> getUser(Authentication authentication){
        String username = authentication.getName();

        return Mono.just(username);
    }

    @GetMapping("oidcToken")
    public Mono<String> getUserOIDCToken(@AuthenticationPrincipal OidcUser oidcUser){
        return Mono.just(oidcUser.getIdToken().getTokenValue());
    }
}
