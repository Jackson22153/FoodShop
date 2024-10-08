package com.phucx.notification.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public final static String REGISTER_ENDPOINT = "/chat";
    public final static String APPLICATION_ENDPOINT = "/app";
    public final static String QUEUE_MESSAGES = "/queue/messages";
    public final static String SIMP_USER = "simpUser";
    public final static String TOPIC_ORDER = "/topic/order";
    public final static String TOPIC_EMPLOYEE_NOTIFICAITON_ORDER = "/topic/employee.notification.order";
    public final static String TOPIC_EMPLOYEE_NOTIFICAITON_ACCOUNT = "/topic/employee.notification.account";

    @Value("${spring.rabbitmq.username}")
    private String rabbitmqAdminUsername;
    @Value("${spring.rabbitmq.password}")
    private String rabbitmqAdminPassword;
    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;
    @Value("${websocket.port}")
    private Integer stompPort;
    @Value("${phucx.ui-url}")
    private String uiUrl;
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwtSetUri;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOrigins(uiUrl);
        registry.addEndpoint("/chat").setAllowedOrigins(uiUrl).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableStompBrokerRelay("/queue/", "/topic", "/exchange")
            .setRelayHost(rabbitmqHost).setRelayPort(stompPort)
            .setSystemLogin(rabbitmqAdminUsername).setSystemPasscode(rabbitmqAdminPassword)
            .setClientLogin(rabbitmqAdminUsername).setClientPasscode(rabbitmqAdminPassword);
        registry.setUserDestinationPrefix("/user");   
    }
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        converter.setObjectMapper(objectMapper);
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
                // log.info("presend clientInboundChannel: {}", message.getPayload().getClass().getName());
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                    // log.info("Message connected!");
                    String token = accessor.getFirstNativeHeader("Authorization");
                    if(token!=null){
                        token = token.substring(7);
                        Jwt jwt =NimbusJwtDecoder.withJwkSetUri(jwtSetUri).build().decode(token);
                        // log.info("UserID: {}", jwt.getSubject());
                        RoleConverter roleConverter = new RoleConverter();
                        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(jwt, roleConverter.convert(jwt));
                        // UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        //     jwt.getSubject(), null, jwtAuthenticationToken.getAuthorities());
                        accessor.setUser(jwtAuthenticationToken);
                    }
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
}
