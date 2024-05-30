package com.phucx.account.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageQueueConfig {
    // account notification 
    public final static String NOTIFICATION_QUEUE = "accountnotification";
    public final static String NOTIFICATION_ROUTING_KEY = "accountnotification";
    //  user queue
    public final static String USER_QUEUE = "userqueue";
    public final static String USER_ROUTING_KEY = "userqueue";

    private final String ACCOUNT_EXCHANGE = "accountservice";
    // account direct exchange
    @Bean
    public DirectExchange accountExchange(){
        return new DirectExchange(ACCOUNT_EXCHANGE);
    }
    // notification queue
    @Bean
    public Queue notificationQueue(){
        return new Queue(NOTIFICATION_QUEUE, false);
    }
    @Bean
    public Binding bindingNotificationQueue(Queue notificationQueue, DirectExchange accountExchange){
        return BindingBuilder.bind(notificationQueue).to(accountExchange).with(NOTIFICATION_ROUTING_KEY);
    }
    // user queue
    @Bean
    public Queue userQueue(){
        return new Queue(USER_QUEUE, false);
    }
    @Bean
    public Binding bindingUserQueue(Queue userQueue, DirectExchange accountExchange){
        return BindingBuilder.bind(userQueue).to(accountExchange).with(USER_ROUTING_KEY);
    }

    // // message queue configuration
    // @Bean
    // public MessageConverter messageConverter() {
    //     Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
    //     return converter;
    // }
    // @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
