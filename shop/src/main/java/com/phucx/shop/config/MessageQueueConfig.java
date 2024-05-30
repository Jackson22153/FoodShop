package com.phucx.shop.config;

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
    public final static String PRODUCT_QUEUE = "productqueue";
    public final static String PRODUCT_ROUTING_KEY = "productqueue";

    public final static String DISCOUNT_QUEUE = "discountqueue";
    public final static String DISCOUNT_ROUTING_KEY = "discountqueue";

    private final String SHOP_EXCHANGE = "shopservice";
    // direct exchange
    @Bean
    public DirectExchange shopExchange(){
        return new DirectExchange(SHOP_EXCHANGE);
    }
    // product message queue
    @Bean
    public Queue productQueue(){
        return new Queue(PRODUCT_QUEUE, false);
    }
    @Bean
    public Binding productBinding(Queue productQueue, DirectExchange shopExchange){
        return BindingBuilder.bind(productQueue).to(shopExchange).with(PRODUCT_ROUTING_KEY);
    }
    // discount message quue
    @Bean
    public Queue discountQueue(){
        return new Queue(DISCOUNT_QUEUE, false);
    }
    @Bean
    public Binding discountBinding(Queue discountQueue, DirectExchange shopExchange){
        return BindingBuilder.bind(discountQueue).to(shopExchange).with(DISCOUNT_ROUTING_KEY);
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
