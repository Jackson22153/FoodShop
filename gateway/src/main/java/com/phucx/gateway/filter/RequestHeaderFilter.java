package com.phucx.gateway.filter;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Order(1)
@Component
public class RequestHeaderFilter implements GlobalFilter{
    private Logger logger = LoggerFactory.getLogger(RequestHeaderFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.fromRunnable(()->{
            logger.info("RequestHeaderFilter, id: {}", UUID.randomUUID().toString());
        }).then(chain.filter(exchange));
    }
    
}
