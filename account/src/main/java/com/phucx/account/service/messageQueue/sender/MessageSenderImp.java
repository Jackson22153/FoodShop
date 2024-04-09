package com.phucx.account.service.messageQueue.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderWithProducts;

@Service
public class MessageSenderImp implements MessageSender{
    @Autowired
    private RabbitTemplate rabbitTemplate;
    // @Override
    // public void send(String exchange, String routingKey, UserOrderProducts userOrderProducts){
    //     this.rabbitTemplate.convertAndSend(exchange, routingKey, userOrderProducts);
    // }
    @Override
    public void send(String exchange, String routingKey, OrderWithProducts order) {
        this.rabbitTemplate.convertAndSend(exchange, routingKey, order);
    }
    @Override
    public NotificationMessage sendAndReceiveOrder(String exchange, String routingKey, OrderWithProducts order) {
        NotificationMessage response = (NotificationMessage) this.rabbitTemplate.convertSendAndReceive(exchange, routingKey, order);
        return response;
    }
    
}
