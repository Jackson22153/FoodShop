package com.phucx.account.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageQueueConfig {
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
}
