package com.phucx.shop.constant;

public class MessageQueueConstant {
    // order exchange
    public final static String ORDER_EXCHANGE = "orderservice";
    // account exchange
    public final static String ACCOUNT_EXCHANGE = "accountservice";
    
    // user routing key
    public final static String USER_ROUTING_KEY = "userqueue";
    //  customer routing key
    public final static String CUSTOMER_ROUTING_KEY = "customerqueue";
    //  employee routing key
    public final static String EMPLOYEE_ROUTING_KEY = "employeequeue";
    //  shipper routing key
    public final static String SHIPPER_ROUTING_KEY = "shipperqueue";
    // order routing key
    public final static String ORDER_ROUTING_KEY = "orderqueue";
}
