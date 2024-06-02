package com.phucx.order.eventListener;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.order.config.MessageQueueConfig;
import com.phucx.order.constant.EventType;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.InvoiceDetails;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderDTO;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.ORDER_QUEUE)
public class OrderMessageListener {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitHandler
    public String fetchOrderData(String message){
        log.info("fetchOrderData({})", message);
        String eventID = UUID.randomUUID().toString();
        EventMessage<Object> responseMessage= new EventMessage<>();
        responseMessage.setEventId(eventID);
        try {
            TypeReference<EventMessage<OrderDTO>> typeRef = new TypeReference<EventMessage<OrderDTO>>() {};
            EventMessage<OrderDTO> orderDOrderDTO =  objectMapper.readValue(message, typeRef);
            // fetch data
            OrderDTO payload = orderDOrderDTO.getPayload();
            if(orderDOrderDTO.getEventType().equals(EventType.GetOrdersByCustomerID)){
                // get orders by customerId
                String customerID = payload.getCustomerID();
                Page<OrderDetails> orders = orderService.getOrdersByCustomerID(customerID, payload.getPageNumber(), payload.getPageSize());
                // set response message
                responseMessage.setPayload(orders);
                responseMessage.setEventType(EventType.ReturnOrdersByCustomerID);
            }else if(orderDOrderDTO.getEventType().equals(EventType.GetOrderInvoiceByIdAndCustomerID)){
                // get order invoice by customerId
                String customerID = payload.getCustomerID();
                String orderID = payload.getOrderID();
                InvoiceDetails invoice = orderService.getInvoiceByCustomerID(customerID, orderID);
                // set response message
                responseMessage.setPayload(invoice);
                responseMessage.setEventType(EventType.ReturnOrderInvoiceByIdAndCustomerID);
            }else if(orderDOrderDTO.getEventType().equals(EventType.GetOrdersByEmployeeID)){
                // get orders by employeeId
                String employeeID = payload.getEmployeeID();
                Page<OrderDetails> orders = orderService.getOrdersByEmployeeID(
                    employeeID, payload.getOrderStatus(), payload.getPageNumber(), payload.getPageSize());
                // set response message
                responseMessage.setPayload(orders);
                responseMessage.setEventType(EventType.ReturnOrdersByEmployeeID);
            }else if(orderDOrderDTO.getEventType().equals(EventType.GetOrderByEmployeeID)){
                // get order by employeeId
                String employeeID = payload.getEmployeeID();
                OrderWithProducts order = orderService.getOrderByEmployeeID(employeeID, payload.getOrderID());
                // set response message
                responseMessage.setPayload(order);
                responseMessage.setEventType(EventType.ReturnOrderByEmployeeID);
            }
            String response = objectMapper.writeValueAsString(responseMessage);
            return response;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }

    }
}
