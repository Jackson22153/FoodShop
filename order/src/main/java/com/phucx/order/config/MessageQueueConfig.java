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

    public final static String ORDER_EXCHANGE = "orderservice";
    // creating exchange key for order service
    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(ORDER_EXCHANGE);
    }

    // order queue
    @Bean
    public Queue orderQueue(){
        return new Queue(ORDER_QUEUE, false);
    }
    @Bean
    public Binding bindingOrderQueue(Queue orderQueue, DirectExchange orderExchange){
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    }

    // order processing queue
    @Bean
    public Queue orderProcessingQueue(){
        return new Queue(ORDER_PROCESSING_QUEUE, false);
    }
    @Bean
    public Binding bindingOrderProcessingQueue(Queue orderProcessingQueue, DirectExchange orderExchange){
        return BindingBuilder.bind(orderProcessingQueue).to(orderExchange).with(ORDER_PROCESSING_QUEUE);
    }

    // message queue configuration
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}
