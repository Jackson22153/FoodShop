package com.phucx.shop.messageQueueListener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.stereotype.Component;

import com.phucx.shop.config.MessageQueueConfig;
import com.phucx.shop.model.ProductInfo;

@Component
@RabbitListeners({
    @RabbitListener(queues = MessageQueueConfig.PRODUCT_QUEUE)
})
public class MessageQueueListener {
    
    @RabbitHandler
    public ProductInfo fetchProduct(ProductInfo productInfo){
        return null;
    }
}
