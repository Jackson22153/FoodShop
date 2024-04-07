package com.phucx.account.eventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.config.MessageQueueConfig;

@Component
@RabbitListener(queues = MessageQueueConfig.ORDER_QUEUE)
public class OrdersListener {
    private Logger logger = LoggerFactory.getLogger(OrdersListener.class);

    // @RabbitHandler
    // public void receiver(OrderWithProducts order){
    //     validateOrder(order);
    // }

    @RabbitHandler
    public Integer receiver(OrderWithProducts order){
        Integer orderID = validateOrder(order);
        return orderID;
    }


    private Integer validateOrder(OrderWithProducts order){
        logger.info(order.toString());
        return 1234;
    }

}
