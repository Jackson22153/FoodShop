package com.phucx.order.eventListener;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.constant.EventType;
import com.phucx.model.EventMessage;
import com.phucx.order.config.MessageQueueConfig;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.InvoiceDetails;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.ResponseFormat;
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
            TypeReference<EventMessage<LinkedHashMap<String, Object>>> typeRef = 
                new TypeReference<EventMessage<LinkedHashMap<String, Object>>>() {};
            EventMessage<LinkedHashMap<String, Object>> eventMessage =  objectMapper.readValue(message, typeRef);
            // fetch data
            LinkedHashMap<String, Object> payload = eventMessage.getPayload();
            if(eventMessage.getEventType().equals(EventType.GetOrderInvoiceByIdAndCustomerID)){
                // get order invoice by customerId
                String customerID = payload.get("customerID").toString();
                String orderID = payload.get("orderID").toString();
                InvoiceDetails invoice = orderService.getInvoiceByCustomerID(customerID, orderID);
                // set response message
                responseMessage.setPayload(invoice);
                responseMessage.setEventType(EventType.ReturnOrderInvoiceByIdAndCustomerID);
            }else if(eventMessage.getEventType().equals(EventType.GetOrderByEmployeeID)){
                // get order by employeeId
                String employeeID = payload.get("employeeID").toString();
                String orderID = payload.get("orderID").toString();
                OrderWithProducts order = orderService.getOrderByEmployeeID(employeeID, orderID);
                // set response message
                responseMessage.setPayload(order);
                responseMessage.setEventType(EventType.ReturnOrderByEmployeeID);
            }else if(eventMessage.getEventType().equals(EventType.GetOrderByOrderID)){
                String orderID = payload.get("orderID").toString();
                OrderDetails orderDetails = getOrderDetails(orderID);
                // set response message
                responseMessage.setPayload(orderDetails);
                responseMessage.setEventType(EventType.ReturnOrder);
            }else if(eventMessage.getEventType().equals(EventType.UpdateOrderStatusAsCanceled)){
                String orderID = payload.get("orderID").toString();
                Boolean status = orderService.updateOrderStatus(orderID, OrderStatus.Canceled);
                // set response message
                ResponseFormat responseFormat = new ResponseFormat(status);
                responseMessage.setPayload(responseFormat);
                responseMessage.setEventType(EventType.ReturnUpdateOrderStatus);
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

    private OrderDetails getOrderDetails(String orderID) throws JsonProcessingException, NotFoundException{
        return orderService.getOrder(orderID);
    }
    

    private <T> EventMessage<T> createResponseMessage(Class<T> type){
        EventMessage<T> responseMessage = new EventMessage<>();
        String eventID = UUID.randomUUID().toString();
        responseMessage.setEventId(eventID);
        return responseMessage;
    }
}
