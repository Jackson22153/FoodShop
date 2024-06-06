package com.phucx.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageQueueConfig {
    // customer notification 
    public final static String NOTIFICATION_CUSTOMER_QUEUE = "notificationcustomerqueue";
    public final static String NOTIFICATION_CUSTOMER_ROUTING_KEY = "notificationcustomerqueue";

    // employee notification 
    public final static String NOTIFICATION_EMPLOYEE_QUEUE = "notificationemployeequeue";
    public final static String NOTIFICATION_EMPLOYEE_ROUTING_KEY = "notificationemployeequeue";

    // order notification
    public final static String NOTIFICATION_ORDER_QUEUE = "notificationorderqueue";
    public final static String NOTIFICATION_ORDER_ROUTING_KEY = "notificationorderqueue";
    // shop notificaiton
    public final static String NOTIFICATION_SHOP_QUEUE = "notificationshopqueue";
    public final static String NOTIFICATION_SHOP_ROUTING_KEY = "notificationshopqueue";

    // notification direct exchange
    private final String NOTIFICATION_EXCHANGE = "notificationservice";
    @Bean
    public DirectExchange notificationExchange(){
        return new DirectExchange(NOTIFICATION_EXCHANGE);
    }
    // customer notification queue
    @Bean
    public Queue notificationCustomerQueue(){
        return new Queue(NOTIFICATION_CUSTOMER_QUEUE, false);
    }
    @Bean
    public Binding bindingNotificationCustomerQueue(Queue notificationCustomerQueue, DirectExchange notificationExchange){
        return BindingBuilder.bind(notificationCustomerQueue).to(notificationExchange).with(NOTIFICATION_CUSTOMER_ROUTING_KEY);
    }
    // employee notification queue
    @Bean
    public Queue notificationEmployeeQueue(){
        return new Queue(NOTIFICATION_EMPLOYEE_QUEUE, false);
    }
    @Bean
    public Binding bindingNotificationEmployeeQueue(Queue notificationEmployeeQueue, DirectExchange notificationExchange){
        return BindingBuilder.bind(notificationEmployeeQueue).to(notificationExchange).with(NOTIFICATION_EMPLOYEE_ROUTING_KEY);
    }
    // order notification queue
    @Bean
    public Queue notificationOrderQueue(){
        return new Queue(NOTIFICATION_ORDER_QUEUE, false);
    }
    @Bean
    public Binding bindingNotificationOrderQueue(Queue notificationOrderQueue, DirectExchange notificationExchange){
        return BindingBuilder.bind(notificationOrderQueue).to(notificationExchange).with(NOTIFICATION_ORDER_ROUTING_KEY);
    }
    // shop notification queue
    @Bean
    public Queue notificationShopQueue(){
        return new Queue(NOTIFICATION_SHOP_QUEUE, false);
    }
    @Bean
    public Binding bindingNotificationShopQueue(Queue notificationShopQueue, DirectExchange notificationExchange){
        return BindingBuilder.bind(notificationShopQueue).to(notificationExchange).with(NOTIFICATION_SHOP_ROUTING_KEY);
    }
    
}
