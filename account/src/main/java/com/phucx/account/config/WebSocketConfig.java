package com.phucx.account.config;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static String REGISTER_ENDPOINT = "/chat";
    public static String APPLICATION_ENDPOINT = "/app";
    // public static String USER_ENDPOINT = "/secure/user";
    public static String QUEUE_MESSAGES = "/queue/messages";
    // @Autowired
    // private UserDestinationMessageHandler userDestinationMessageHandler;

    public static String SIMP_USER = "simpUser";
    // @Autowired
    // private ApplicationContext context;

    private Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOrigins("http://localhost:5173");
        registry.addEndpoint("/chat").setAllowedOrigins("http://localhost:5173").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        // registry.enableStompBrokerRelay("/queue/", "/topic", "/exchange", "/temp-queue")
        //     .setRelayHost("localhost").setRelayPort(61613)
        //     .setSystemLogin("admin").setSystemPasscode("123")
        //     .setClientLogin("client").setClientPasscode("123");
        registry.setUserDestinationPrefix("/user");
        registry.enableSimpleBroker("/user", "/topic");    
    }
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false;
    }

    // authentication websocket with jwt 
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor(){
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                logger.info("presend clientInboundChannel: {}", message.getPayload().getClass().getName());
                // logger.info("UserDestinationMessageHandler: {}", userDestinationMessageHandler.getBroadcastDestination() );
            
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                    JwtAuthenticationToken jwt = message.getHeaders().get(SIMP_USER, JwtAuthenticationToken.class);
                    Authentication user = new UsernamePasswordAuthenticationToken(
                        jwt.getPrincipal(), null, jwt.getAuthorities());
                    accessor.setUser(user);
                }
                return message;
            }
        });
    }


    // disable csrf
    @Order(Ordered.HIGHEST_PRECEDENCE+99)
    @Bean(name = "csrfChannelInterceptor")
    public ChannelInterceptor csrfChannelInterceptor(){
        return new CustomCsrfChannelInterceptor();
    }

    // @Order(Ordered.HIGHEST_PRECEDENCE+99)
    // @Bean(name = "UserDestinationMessageHandler")
    // public UserDestinationMessageHandler customUserDestinationMessageHandler(SubscribableChannel clientInboundChannel,
    // SubscribableChannel brokerChannel, UserDestinationResolver destinationResolver){
    //     return new CustomUserDestinationMessageHandler(clientInboundChannel, brokerChannel, destinationResolver);
    // }
}
