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
    //  customer queue
    public final static String CUSTOMER_QUEUE = "customerqueue";
    public final static String CUSTOMER_ROUTING_KEY = "customerqueue";
    //  employee queue
    public final static String EMPLOYEE_QUEUE = "employeequeue";
    public final static String EMPLOYEE_ROUTING_KEY = "employeequeue";
    //  shipper queue
    public final static String SHIPPER_QUEUE = "shipperqueue";
    public final static String SHIPPER_ROUTING_KEY = "shipperqueue";

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
    // customer queue
    @Bean
    public Queue customerQueue(){
        return new Queue(CUSTOMER_QUEUE, false);
    }
    @Bean
    public Binding bindingCustomerQueue(Queue customerQueue, DirectExchange accountExchange){
        return BindingBuilder.bind(customerQueue).to(accountExchange).with(CUSTOMER_ROUTING_KEY);
    }
    // employee queue
    @Bean
    public Queue employeeQueue(){
        return new Queue(EMPLOYEE_QUEUE, false);
    }
    @Bean
    public Binding bindingEmployeeQueue(Queue employeeQueue, DirectExchange accountExchange){
        return BindingBuilder.bind(employeeQueue).to(accountExchange).with(EMPLOYEE_ROUTING_KEY);
    }
    // shipper queue
    @Bean
    public Queue shipperQueue(){
        return new Queue(SHIPPER_QUEUE, false);
    }
    @Bean
    public Binding bindingShipperQueue(Queue shipperQueue, DirectExchange accountExchange){
        return BindingBuilder.bind(shipperQueue).to(accountExchange).with(SHIPPER_ROUTING_KEY);
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
