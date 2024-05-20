package com.phucx.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.phucx.gateway.service.KeycloakAuthService;

import reactor.core.publisher.Mono;

@RestController
public class HomeController {
    // private ServerRedirectStrategy redirectStrategy;
    @Autowired
    private KeycloakAuthService loginService;

    @GetMapping("/loginBE")
    public Mono<Void> login(ServerWebExchange exchange){
        return loginService.login(exchange);
    }

    @GetMapping("/loginBE/oauth2/code")
    public Mono<Void> codeExchange(@RequestParam(name = "code") String code, ServerWebExchange exchange){
        return this.loginService.exchangeCode(exchange, code);
    }

    @PostMapping("/logoutBE")
    public Mono<Void> logoutBE(@CookieValue(name = "idtoken") String idtoken, ServerWebExchange exchange){
        // MultiValueMap<String, HttpCookie>  exchange.getRequest().getCookies();
        return loginService.logout(exchange, idtoken);
    }

    // @PostMapping("isAuthenticated")
    // public Mono<UserAuthenticationInfo> checkAuthentication(
    //     @AuthenticationPrincipal OidcUser oidcUser,
    //     Authentication authentication){

    //     return Mono.just(new UserAuthenticationInfo()).map(info ->{
    //         if(authentication!=null){
    //             String userID = oidcUser.getSubject();
    //             info.setUserID(userID);
    //             info.setUsername(authentication.getName());
    //             info.setAuthenticated(true);
    //         }else{
    //             info.setAuthenticated(false);
    //         }
    //         return info;
    //     });
    // }

//     @GetMapping("user")
//     public Mono<String> getUser(Authentication authentication){
//         String username = authentication.getName();

//         return Mono.just(username);
//     }

//     @GetMapping("oidcToken")
//     public Mono<String> getUserOIDCToken(@AuthenticationPrincipal OidcUser oidcUser){
//         return Mono.just(oidcUser.getIdToken().getTokenValue());
//     }
}
