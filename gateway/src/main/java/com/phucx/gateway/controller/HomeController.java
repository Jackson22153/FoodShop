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
import com.phucx.gateway.model.UserAuthenticationInfo;

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
    public Mono<Void> login(ServerWebExchange exchange, Authentication authentication){
        System.out.println("userID: " + authentication.getName());
        redirectStrategy = new DefaultServerRedirectStrategy();
        return redirectStrategy.sendRedirect(exchange, URI.create(GatewayConstant.FoodShopUrl));
    }

    @PostMapping("isAuthenticated")
    public Mono<UserAuthenticationInfo> checkAuthentication(Authentication authentication){
        return Mono.just(new UserAuthenticationInfo()).map(info ->{
            if(authentication!=null){
                info.setUsername(authentication.getName());
                info.setAuthenticated(true);
            }else{
                info.setAuthenticated(false);
            }
            return info;
        });
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
