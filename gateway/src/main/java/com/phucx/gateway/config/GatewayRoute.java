package com.phucx.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoute {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
            .route(route -> route
                .path("/shop/**")
                .filters(filter -> filter
                    .rewritePath("/shop/(?<segment>.*)","/${segment}"))
                .uri("lb://SHOP"))
            .route(route -> route
                .path("/account/**")
                .filters(filter -> filter
                    .removeRequestHeader("Cookie")
                    .rewritePath("/account/(?<segment>.*)","/${segment}"))
                .uri("lb://ACCOUNT"))
            .build();
    }
}
