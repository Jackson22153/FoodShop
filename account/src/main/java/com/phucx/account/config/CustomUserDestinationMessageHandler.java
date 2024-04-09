package com.phucx.account.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.simp.stomp.StompBrokerRelayMessageHandler;
import org.springframework.messaging.simp.user.DefaultUserDestinationResolver;
import org.springframework.messaging.simp.user.UserDestinationMessageHandler;
import org.springframework.messaging.simp.user.UserDestinationResolver;

public class CustomUserDestinationMessageHandler extends UserDestinationMessageHandler{
    private Logger logger = LoggerFactory.getLogger(CustomUserDestinationMessageHandler.class);
    public CustomUserDestinationMessageHandler(SubscribableChannel clientInboundChannel,
            SubscribableChannel brokerChannel, UserDestinationResolver destinationResolver) {
        super(clientInboundChannel, brokerChannel, destinationResolver);
    }
    @Override
    public UserDestinationResolver getUserDestinationResolver() {
        UserDestinationResolver u = super.getUserDestinationResolver();
        System.out.println("getUserDestinationResolver");
        return u;
    }
    @Override
    public void handleMessage(Message<?> sourceMessage) throws MessagingException {
        super.handleMessage(sourceMessage);
        logger.info(sourceMessage.getHeaders().toString());
        try {
            var result = getUserDestinationResolver().resolveDestination(sourceMessage);
            System.out.println("Result");
            logger.info("SourceDestination {}", result.getSourceDestination());
            logger.info("SessionIds {}", result.getSessionIds());
            logger.info("subscibeDestion {}", result.getSubscribeDestination());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
  
}
