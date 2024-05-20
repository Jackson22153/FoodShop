package com.phucx.gateway.service;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import com.phucx.gateway.config.KeycloakProperties;
import com.phucx.gateway.constant.GatewayConstant;
import com.phucx.gateway.model.Oauth2Token;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class KeycloakAuthService {
    @Autowired
    private KeycloakProperties keycloakProperties;
    @Autowired
    private WebClient webClient;
    private ServerRedirectStrategy redirectStrategy;

    private final static String ACCESS_TOKEN_COOKIE = "accesstoken";
    private final static String ID_TOKEN_COOKIE = "idtoken";
    private final static String REFRESH_TOKEN_COOKIE = "refreshtoken";

    public KeycloakAuthService() {
        redirectStrategy = new DefaultServerRedirectStrategy();
    }
    @Autowired
    public Mono<Void> setKeycloakProperties(KeycloakProperties keycloakProperties) {
        return Mono.just(this.keycloakProperties = keycloakProperties).then();
    }
    @Autowired
    public Mono<Void> setWebClient(WebClient webClient) {
        return Mono.just(this.webClient = webClient).then();
    }

    public Mono<Void> login(ServerWebExchange exchange){
        String authorizationUri = keycloakProperties.getAuthorizationUri();
        String authUri = authorizationUri.replace("keycloak", "localhost");
        return Mono.just(authUri).map(uri -> UriComponentsBuilder
            .fromUriString(authUri)
            .queryParam("client_id", keycloakProperties.getClientId())
            .queryParam("scope", keycloakProperties.getScope())
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", keycloakProperties.getRedirectUri())
        ).flatMap(uri -> redirectStrategy.sendRedirect(exchange, uri.build().toUri()));
    }



    public Mono<Oauth2Token> getToken(String code){
        String tokenUri = keycloakProperties.getTokenUri();
        log.info("tokenUri: {}", tokenUri);
        return Mono.just(tokenUri)
            .map(uri -> UriComponentsBuilder.fromUriString(tokenUri).build())
            .flatMap(uri -> {
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("client_secret", keycloakProperties.getClientSecret());
                formData.add("client_id", keycloakProperties.getClientId());
                formData.add("code", code);
                formData.add("redirect_uri", keycloakProperties.getRedirectUri());
                formData.add("grant_type", "authorization_code");

                formData.entrySet().stream().forEach(value -> {
                    log.info("{}: {}",value.getKey(), value.getValue());
                });
                return webClient.post().uri(uri.toUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData).retrieve().toEntity(Oauth2Token.class);
            }).map(res ->{
                Oauth2Token token = new Oauth2Token();
                if(res.getStatusCode().is2xxSuccessful()){
                    token = res.getBody();
                }
                return token;
            });
    }
    public Mono<Void> exchangeCode(ServerWebExchange exchange, String code){
        try {
            return this.getToken(code).doOnNext(token -> {
                    // access token
                    ResponseCookie accessTokenCookie = ResponseCookie.from("accesstoken", token.getAccess_token())
                        .path("/")
                        .build();
                    exchange.getResponse().addCookie(accessTokenCookie);
                    // access token
                    ResponseCookie idTokenCookie = ResponseCookie.from("idtoken", token.getId_token())
                        .path("/")
                        .build();
                    exchange.getResponse().addCookie(idTokenCookie);
                    // ResponseCookie refreshCookie = ResponseCookie.from("refreshtoken", token.getRefresh_token())
                    //     .path("/")
                    //     .build();
                    // exchange.getResponse().addCookie(refreshCookie);
                }).then(redirectStrategy.sendRedirect(exchange, URI.create(GatewayConstant.FoodShopUrl)));
        } catch (Exception e) {
            return redirectStrategy.sendRedirect(exchange, URI.create(GatewayConstant.FoodShopUrl));
        }
    }

    public Mono<Void> logout(ServerWebExchange exchange, String idtoken){
        log.info("idtoken: {}", idtoken);
        String logoutURL = keycloakProperties.getIssuer()+"/protocol/openid-connect/logout";
        return Mono.just(UriComponentsBuilder
            .fromUriString(logoutURL)
            .queryParam("id_token_hint", idtoken)
        ).flatMap(builder -> webClient.get().uri(builder.toUriString())
            .retrieve().toEntity(String.class)
        ).doOnNext(response ->{
            if(response.getStatusCode().is2xxSuccessful()){
                log.info("Login successfully");
                // remove cookie
                MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
                if(cookies.containsKey(ACCESS_TOKEN_COOKIE)){
                    ResponseCookie removedCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, null)
                        .maxAge(0)
                        .path("/")
                        .build();
                    exchange.getResponse().addCookie(removedCookie);
                }
                if(cookies.containsKey(ID_TOKEN_COOKIE)){
                    ResponseCookie removedCookie = ResponseCookie.from(ID_TOKEN_COOKIE, null)
                        .maxAge(0)
                        .path("/")
                        .build();
                    exchange.getResponse().addCookie(removedCookie);
                }
                if(cookies.containsKey(REFRESH_TOKEN_COOKIE)){
                    ResponseCookie removedCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, null)
                        .maxAge(0)
                        .path("/")
                        .build();
                    exchange.getResponse().addCookie(removedCookie);
                }
            }else {
                log.error("Logout failed");
                // System.out.println("Logout failed");
            }
        }).then();
    }
}
