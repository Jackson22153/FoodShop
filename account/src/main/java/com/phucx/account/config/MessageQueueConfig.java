package com.phucx.account.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageQueueConfig {
    // message queue 
    // public final static String ORDER_QUEUE = "order";
    // public final static String ORDER_ROUTING_KEY = "order";

    public final static String NOTIFICATION_QUEUE = "accountnotification";
    public final static String NOTIFICATION_ROUTING_KEY = "accountnotification";
    // creating order message queue
    // @Bean
    // public Queue orderQueue(){
    //     return new Queue(ORDER_QUEUE, false);
    // }
    // creating exchange key for order message queue
    // @Bean
    // public DirectExchange orderExchange(){
    //     return new DirectExchange(ORDER_ROUTING_KEY);
    // }
    // binding exchange key to order message queue
    // @Bean
    // public Binding bindingOrderQueue(Queue orderQueue, DirectExchange orderExchange){
    //     return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    // }

    // creating notification message queue
    @Bean
    public Queue notificationQueue(){
        return new Queue(NOTIFICATION_QUEUE, false);
    }
    // creating exchange key for notification message queue
    @Bean
    public DirectExchange notificationExchange(){
        return new DirectExchange(NOTIFICATION_ROUTING_KEY);
    }
    // binding exchange key to notification message queue
    @Bean
    public Binding bindingNotificationQueue(Queue notificationQueue, DirectExchange notificationExchange){
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
