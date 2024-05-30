package com.phucx.order.config;

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
    // order queue 
    public final static String ORDER_QUEUE = "orderqueue";
    public final static String ORDER_ROUTING_KEY = "orderqueue";
    // order process queue 
    public final static String ORDER_PROCESSING_QUEUE = "orderprocessingqueue";
    public final static String ORDER_PROCESSING_ROUTING_KEY = "orderprocessingqueue";
    // order notification queue
    public final static String ORDER_NOTIFICATION_QUEUE = "ordernotification";
    public final static String ORDER_NOTIFICATION_ROUTING_KEY = "ordernotification";

    private final String ORDER_EXCHANGE = "orderservice";
    // creating exchange key for order service
    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(ORDER_EXCHANGE);
    }

    // order queue
    // creating order message queue
    @Bean
    public Queue orderQueue(){
        return new Queue(ORDER_QUEUE, false);
    }
    // binding exchange key to order message queue
    @Bean
    public Binding bindingOrderQueue(Queue orderQueue, DirectExchange orderExchange){
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    }

    // creating notification message queue
    @Bean
    public Queue notificationQueue(){
        return new Queue(ORDER_NOTIFICATION_QUEUE, false);
    }
    // notification queue
    // binding exchange key to notification message queue
    @Bean
    public Binding bindingNotificationQueue(Queue notificationQueue, DirectExchange orderExchange){
        return BindingBuilder.bind(notificationQueue).to(orderExchange).with(ORDER_NOTIFICATION_ROUTING_KEY);
    }

    // message queue configuration
    // @Bean
    // public MessageConverter jsonMessageConverter(){
    //     return new Jackson2JsonMessageConverter();
    // }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}
