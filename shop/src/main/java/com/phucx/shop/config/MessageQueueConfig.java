package com.phucx.shop.config;

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
    public final static String PRODUCT_QUEUE = "productservice";
    public final static String PRODUCT_ROUTING_KEY = "productservice";

    // product service message queue
    @Bean
    public DirectExchange productServiceExchange(){
        return new DirectExchange(PRODUCT_ROUTING_KEY);
    }
    @Bean
    public Queue productService(){
        return new Queue(PRODUCT_QUEUE, false);
    }
    @Bean
    public Binding productBinding(Queue productService, DirectExchange productDirectExchange){
        return BindingBuilder.bind(productService).to(productDirectExchange).with(PRODUCT_ROUTING_KEY);
    }

    // message queue configuration 
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
