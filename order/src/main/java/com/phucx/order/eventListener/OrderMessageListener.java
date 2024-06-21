package com.phucx.order.eventListener;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.order.config.MessageQueueConfig;
import com.phucx.order.constant.EventType;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.InvoiceDetails;
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
        EventMessage<Object> responseMessage= createResponseMessage(Object.class);
        try {
            TypeReference<EventMessage<OrderDTO>> typeRef = new TypeReference<EventMessage<OrderDTO>>() {};
            EventMessage<OrderDTO> orderDOrderDTO =  objectMapper.readValue(message, typeRef);
            // fetch data
            OrderDTO payload = orderDOrderDTO.getPayload();
            if(orderDOrderDTO.getEventType().equals(EventType.GetOrderInvoiceByIdAndCustomerID)){
                // get order invoice by customerId
                String customerID = payload.getCustomerID();
                String orderID = payload.getOrderID();
                InvoiceDetails invoice = orderService.getInvoiceByCustomerID(customerID, orderID);
                // set response message
                responseMessage.setPayload(invoice);
                responseMessage.setEventType(EventType.ReturnOrderInvoiceByIdAndCustomerID);
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
        } catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
            return null;
        } catch (NotFoundException e){
            try {
                log.error("Error: {}", e.getMessage());
                responseMessage.setEventType(EventType.NotFoundException);
                responseMessage.setErrorMessage(e.getMessage());
                String errorResponse = objectMapper.writeValueAsString(responseMessage);    
                return errorResponse;
            } catch (Exception exception) {
                log.error("Error: {}", exception.getMessage());
                return null;
            }
        }

    }

    private <T> EventMessage<T> createResponseMessage(Class<T> type){
        EventMessage<T> responseMessage = new EventMessage<>();
        String eventID = UUID.randomUUID().toString();
        responseMessage.setEventId(eventID);
        return responseMessage;
    }
}
