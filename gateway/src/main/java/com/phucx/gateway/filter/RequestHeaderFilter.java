package com.phucx.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.phucx.gateway.config.KeycloakProperties;
import com.phucx.gateway.model.Oauth2Token;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Order(1)
@Component
public class RequestHeaderFilter implements GlobalFilter{
    private KeycloakProperties keycloakProperties;

    @Autowired
    public void setKeycloakProperties(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    private final String REFRESH_TOKEN_COOKIE = "refreshtoken";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.just("Prefilter").then(chain.filter(exchange));
        // return Mono.just(exchange.getRequest().getCookies()).flatMap(cookies ->{
        //     try {
        //         if(cookies.containsKey(REFRESH_TOKEN_COOKIE)){
        //             String refreshtoken = cookies.getFirst(REFRESH_TOKEN_COOKIE).getValue();
        //             if(refreshtoken!=null){
        //                 MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        //                     formData.add("client_secret", keycloakProperties.getClientSecret());
        //                     formData.add("client_id", keycloakProperties.getClientId());
        //                     formData.add("refresh_token", refreshtoken);
        //                     formData.add("scope", keycloakProperties.getScope());
        //                     formData.add("grant_type", "refresh_token");
        //                     formData.entrySet().stream().forEach(data ->{
        //                         log.info("key: {} value: {}", data.getKey(), data.getValue());
        //                     });
        //                     return WebClient.create(keycloakProperties.getTokenUri())
        //                         .post().contentType(MediaType.APPLICATION_FORM_URLENCODED)
        //                         .bodyValue(formData).retrieve().toEntity(Oauth2Token.class).flatMap(res ->{
        //                             if(res.getStatusCode().is2xxSuccessful()){
        //                                 Oauth2Token token = res.getBody();
        //                                 // access token
        //                                 ResponseCookie accessTokenCookie = ResponseCookie.from("accesstoken", token.getAccess_token())
        //                                     .path("/")
        //                                     .build();
        //                                 exchange.getResponse().addCookie(accessTokenCookie);
        //                                 // access token
        //                                 ResponseCookie idTokenCookie = ResponseCookie.from("idtoken", token.getId_token())
        //                                     .path("/")
        //                                     .build();
        //                                 exchange.getResponse().addCookie(idTokenCookie);
        //                                 ResponseCookie refreshCookie = ResponseCookie.from("refreshtoken", token.getRefresh_token())
        //                                     .path("/")
        //                                     .build();
        //                                 exchange.getResponse().addCookie(refreshCookie);
        //                             }
        //                             return Mono.empty();
        //                         });
        //             }
        //         }
                
        //     } catch (Exception e) {
        //         // TODO: handle exception
        //     }
        //     return Mono.empty();
        // }).then(chain.filter(exchange));
    }

    
}
