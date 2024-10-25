package com.phucx.payment.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScans({
    @ComponentScan("com.phucx.payment.eventListener")
})
public class MessageQueueConfig {
    public final static String PAYMENT_QUEUE = "paymentqueue";
    public final static String PAYMENT_QUEUE_ROUTING_KEY = "paymentqueue";

    public final static String PAYMENT_SERVICE = "paymentservice";

    @Bean
    public Queue paymentQueue(){
        return new Queue(PAYMENT_QUEUE, false);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(PAYMENT_SERVICE);
    }

    @Bean
    public Binding paymentBind(Queue paymentQueue, DirectExchange directExchange){
        return BindingBuilder.bind(paymentQueue).to(directExchange).with(PAYMENT_QUEUE_ROUTING_KEY);
    }
}
