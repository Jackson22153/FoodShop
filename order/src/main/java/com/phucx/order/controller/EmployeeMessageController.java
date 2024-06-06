package com.phucx.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.annotations.LoggerAspect;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.service.order.EmployeeOrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@MessageMapping("/employee")
public class EmployeeMessageController {
    @Autowired
    private EmployeeOrderService employeeOrderService;

    // CONFIRM AN ORDER
    @LoggerAspect
    @MessageMapping("/order/confirm")
    public void confirmOrder(@RequestBody OrderWithProducts order, Authentication authentication) 
    throws InvalidOrderException, JsonProcessingException{
        log.info("confirmOrder(order={}, userID={})", order, authentication.getName());
        // // validate order
        employeeOrderService.confirmOrder(order, authentication.getName());
    }
    // CANCEL AN ORDER
    @LoggerAspect
    @MessageMapping("/order/cancel")
    public void cancelOrder(@RequestBody OrderWithProducts order, Authentication authentication) throws JsonProcessingException{
        log.info("cancelOrder(order={}, userID={})", order, authentication.getName());
        // cancel order
        employeeOrderService.cancelOrder(order, authentication.getName());
    }
    // FULFILL ORDER
    @LoggerAspect
    @MessageMapping("/order/fulfill")
    public void fulfillOrder(@RequestBody OrderWithProducts order, Authentication authentication) throws JsonProcessingException{
        log.info("fulfillOrder(order={}, userID={})", order.getOrderID(), authentication.getName());
        // update order status
        employeeOrderService.fulfillOrder(order);
    }
}
