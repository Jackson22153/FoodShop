package com.phucx.account.service.messageQueue.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.account.model.UserOrderProducts;

@Service
// @RabbitListener(queues = "#{orderQueue.name}")
public class OrderMessageRecieverImp implements MessageReceiver {
    private Logger logger = LoggerFactory.getLogger(OrderMessageRecieverImp.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    // @RabbitHandler
    public void receiver(String order) throws JsonMappingException, JsonProcessingException {
        UserOrderProducts userOrderProducts = objectMapper.readValue(order, UserOrderProducts.class);
        logger.info("Order receiver: " + userOrderProducts.toString());
    }


    
}
