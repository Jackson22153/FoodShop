package com.phucx.order.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.phucx.order.constant.OrderStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderStatusConverter implements Converter<String, OrderStatus> {

    @Override
    public OrderStatus convert(String status) {
        try {
            String con = status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
            return OrderStatus.valueOf(con);
        } catch (Exception e) {
            log.error("Does not have any order status like {}", status);
            return null;
        }
    }
    
}
