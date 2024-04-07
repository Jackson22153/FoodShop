package com.phucx.gateway.filter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Configuration
public class ValidateHeaderFilter {
    private final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private Logger logger = LoggerFactory.getLogger(ValidateHeaderFilter.class);
    // remove duplicate access_control_allow header in case customer/employee connect with websocket of account
    @Bean
    public GlobalFilter validateHeader(){
        return (exchange, chain)->{
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                logger.info("ValidateHeaderFilter");
                ServerHttpResponse response = exchange.getResponse();
                List<String> headers = response.getHeaders().get(ACCESS_CONTROL_ALLOW_ORIGIN);
                List<String> credentials = response.getHeaders().get(ACCESS_CONTROL_ALLOW_CREDENTIALS);
                if(headers.size()>1){
                    String origin = headers.get(0);

                    response.getHeaders().remove(ACCESS_CONTROL_ALLOW_ORIGIN);
                    response.getHeaders().setAccessControlAllowOrigin(origin);
                    logger.info("origin: {}", origin);
                }
                if(credentials.size()>1){
                    response.getHeaders().remove(ACCESS_CONTROL_ALLOW_CREDENTIALS);
                    response.getHeaders().set(ACCESS_CONTROL_ALLOW_CREDENTIALS, credentials.get(0));
                    logger.info("credential: {}", credentials.get(0));
                }
            }));
        };
    }

}
