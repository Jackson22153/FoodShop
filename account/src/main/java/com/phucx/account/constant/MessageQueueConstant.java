package com.phucx.account.constant;

public class MessageQueueConstant {
    // order exchange
    public final static String ORDER_EXCHANGE = "orderservice";
    // shop exchange
    public final static String SHOP_EXCHANGE = "shopservice";
    // keycloak api exchange
    public final static String KEYCLOAK_API_EXCHANGE = "keycloakapiservice";
    public final static String KEYCLOAK_API_ROUTING_KEY = "keycloakapiqueue";
    // notification exchange
    public final static String NOTIFICATION_EXCHANGE = "notificationservice";
    // order routing key
    public final static String ORDER_ROUTING_KEY = "orderqueue";
    // product routing key
    public final static String PRODUCT_ROUTING_KEY = "productqueue";
    // discount routing key
    public final static String DISCOUNT_ROUTING_KEY = "discountqueue";
    // customer notification routing key 
    public final static String CUSTOMER_NOTIFICATION_ROUTING_KEY = "notificationcustomerqueue";
    // employee notification routing key 
    public final static String EMPLOYEE_NOTIFICATION_ROUTING_KEY = "notificationemployeequeue";
}
