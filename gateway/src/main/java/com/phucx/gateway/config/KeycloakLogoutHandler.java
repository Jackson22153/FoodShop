package com.phucx.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

@Component
public class KeycloakLogoutHandler implements ServerLogoutHandler{
    private WebClient webClient;

    @Autowired
    public Mono<Void> setWebClient(WebClient webClient){
        return Mono.just(this.webClient=webClient).then();
    }

    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {   
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        // return logoutFromKeycloak(oidcUser);
        return logoutFromKeycloak(oidcUser);
    }

    

    private Mono<Void> logoutFromKeycloak(OidcUser oidcUser){
        String logoutUrl = oidcUser.getIssuer().toString() + "/protocol/openid-connect/logout";
        return Mono.just(logoutUrl).map(url ->{
                UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("id_token_hint", oidcUser.getIdToken().getTokenValue());
                return builder;
            })
            .flatMap(builder ->{
                return webClient.get().uri(builder.toUriString())
                .retrieve().toEntity(String.class);
            }).doOnNext(response ->{
                if(response.getStatusCode().is2xxSuccessful()){
                    System.out.println("Logout successfully");
                }else {
                    System.out.println("Logout failed");
                }
            })
            .then();
    }

}
