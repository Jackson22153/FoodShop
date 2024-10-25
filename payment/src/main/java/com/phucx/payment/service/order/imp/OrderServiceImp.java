package com.phucx.payment.service.order.imp;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.constant.EventType;
import com.phucx.model.DataDTO;
import com.phucx.model.EventMessage;
import com.phucx.model.OrderDTO;
import com.phucx.payment.constant.MessageQueueConstant;
import com.phucx.payment.exception.NotFoundException;
import com.phucx.payment.model.OrderDetails;
import com.phucx.payment.model.ResponseFormat;
import com.phucx.payment.service.messageQueue.MessageQueueService;
import com.phucx.payment.service.order.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImp implements OrderService{
    @Autowired
    private MessageQueueService messageQueueService;


    @Override
    public void updateOrderStatusAsCanceled(String orderID) throws JsonProcessingException {
        log.info("updateOrderStatusAsCanceled(orderID={})", orderID);
        // create a request for user
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderID(orderID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.UpdateOrderStatusAsCanceled);
        eventMessage.setPayload(orderDTO);
        // receive data
        EventMessage<ResponseFormat> response = messageQueueService.sendAndReceiveData(
            eventMessage, 
            MessageQueueConstant.ORDER_EXCHANGE, 
            MessageQueueConstant.ORDER_ROUTING_KEY,
            ResponseFormat.class);
        log.info("response={}", response);
        // check exception
        if(response.getEventType().equals(EventType.NotFoundException)){
            throw new NotFoundException(response.getErrorMessage());
        }
        ResponseFormat responseFormat = response.getPayload();
        if(!responseFormat.getStatus())
            throw new RuntimeException("Can not update order status as canceled for order " + orderID);

    }

    @Override
    public OrderDetails getOrder(String orderID) throws JsonProcessingException {
        log.info("getOrder(orderID={})", orderID);
        // create a request for user
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderID(orderID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetOrderByOrderID);
        eventMessage.setPayload(orderDTO);
        // receive data
        EventMessage<OrderDetails> response = messageQueueService.sendAndReceiveData(
            eventMessage, 
            MessageQueueConstant.ORDER_EXCHANGE, 
            MessageQueueConstant.ORDER_ROUTING_KEY,
            OrderDetails.class);
        log.info("response={}", response);
        // check exception
        if(response.getEventType().equals(EventType.NotFoundException)){
            throw new NotFoundException(response.getErrorMessage());
        }
        return response.getPayload();
    }
    
}
