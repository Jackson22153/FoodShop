package com.phucx.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoute {
    @Autowired
    private TokenRelayGatewayFilterFactory tokenRelayGatewayFilterFactory;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
            .route(route -> route
                .path("/client/**")
                .filters(filter -> filter
                    .filter(tokenRelayGatewayFilterFactory.apply())
                    .rewritePath("/client/(?<segment>.*)","/${segment}"))
                .uri("lb://CLIENT"))
            .build();
    }
}
