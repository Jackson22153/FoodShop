package com.phucx.gateway.config;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@ComponentScan("com.phucx.gateway.filter")
public class WebConfig {
    private Logger logger = LoggerFactory.getLogger(WebConfig.class);
    @Bean
    public KeycloakLogoutHandler getKeycloakLogoutHandler(){
        return new KeycloakLogoutHandler();
    }

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

        http.csrf(csrf -> csrf.disable()
            // .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
            // .csrfTokenRequestHandler(new XorServerCsrfTokenRequestAttributeHandler()::handle)

            // .requireCsrfProtectionMatcher(new ServerWebExchangeMatcher() {
            //     @Override
            //     public Mono<MatchResult> matches(ServerWebExchange exchange) {
            //         // PathPatternParser pathPatternParser = new PathPatternParser();
            //         // PathPattern path = pathPatternParser.parse("/account/chat/**");

            //         return Mono.just(exchange).filter(e -> {
            //             ServerHttpRequest request = e.getRequest();
            //             // RequestPath requestPath = request.getPath();
            //             boolean notGetMethod = !(request.getMethod().equals(HttpMethod.GET));
            //             return notGetMethod;
            //         }).flatMap(e -> MatchResult.match())
            //         .switchIfEmpty(MatchResult.notMatch());
            //     }
                
            // })
            );
        // http.logout(logout -> logout.logoutHandler(null))
        http.authorizeExchange(request -> request
            // .pathMatchers("/home").permitAll()
            .pathMatchers("/shop/search/**").permitAll()
            .pathMatchers("/shop/home/**").permitAll()
            .pathMatchers("/isAuthenticated").permitAll()
            .pathMatchers("/account/admin/**").permitAll()
            .anyExchange().authenticated());

        http.oauth2Login(Customizer.withDefaults())
            .logout(logout-> logout.logoutHandler(getKeycloakLogoutHandler()));

        http.formLogin(login -> login.disable());
        http.httpBasic(login -> login.disable());
        return http.build();
    }

    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }

    @Bean
    public WebFilter addCsrfToken(){
        return (exchange, chain)->{
            Mono<CsrfToken> csrfToken = exchange.getAttribute(CsrfToken.class.getName());
            if(csrfToken!=null){
                return csrfToken
                .doOnSuccess(token -> {}).then(chain.filter(exchange));
            } return chain.filter(exchange);
        };
    }
}
