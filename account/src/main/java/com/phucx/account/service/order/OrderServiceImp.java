package com.phucx.account.service.order;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.account.constant.EventType;
import com.phucx.account.constant.MessageQueueConstant;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.model.DataDTO;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.InvoiceDetails;
import com.phucx.account.model.OrderDetails;
import com.phucx.account.model.OrderDTO;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImp implements OrderService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public Page<OrderDetails> getCustomerOrders(String customerID, OrderStatus orderStatus, int pageNumber, int pageSize) 
        throws JsonProcessingException {

        log.info("getCustomerOrders(customerID={}, orderStatus={}, pageNumber={}, pageSize={})", 
            customerID, orderStatus, pageNumber, pageSize);
        // create a order DTO
        OrderDTO orderDTO = new OrderDTO(orderStatus, pageNumber, pageSize);
        orderDTO.setCustomerID(customerID);
        // create a event message for order
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetOrdersByCustomerID);
        eventMessage.setPayload(orderDTO);
        // receive data
        TypeReference<Page<OrderDetails>> typeReference = new TypeReference<Page<OrderDetails>>() {};
        EventMessage<Page<OrderDetails>> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ORDER_QUEUE, 
            MessageQueueConstant.ORDER_ROUTING_KEY,
            typeReference);
        log.info("response={}", response);
        return (Page<OrderDetails>) response.getPayload();
    }

    @Override
    public InvoiceDetails getCustomerInvoice(String orderID, String customerID) throws JsonProcessingException {
        log.info("getCustomerInvoice(orderID={}, customerID={})", orderID, customerID);
        // create a DTO for order
        OrderDTO orderDTO = new OrderDTO(orderID);
        orderDTO.setCustomerID(customerID);
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetOrderInvoiceByIdAndCustomerID);
        eventMessage.setPayload(orderDTO);
        // receive data
        EventMessage<InvoiceDetails> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ORDER_QUEUE, 
            MessageQueueConstant.ORDER_ROUTING_KEY,
            InvoiceDetails.class);
        log.info("response={}", response);
        return response.getPayload();
    }

    @Override
    public OrderWithProducts getEmployeeOrder(String orderID, String employeeID) throws JsonProcessingException {
        log.info("getEmployeeOrder(orderID={}, employeeID={})", orderID, employeeID);
        // create a DTO for order
        OrderDTO orderDTO = new OrderDTO(orderID);
        orderDTO.setEmployeeID(employeeID);
        // create a DTO message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetOrderByEmployeeID);
        eventMessage.setPayload(orderDTO);
        // receive data
        EventMessage<OrderWithProducts> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ORDER_QUEUE, 
            MessageQueueConstant.ORDER_ROUTING_KEY,
            OrderWithProducts.class);
        log.info("response={}", response);
        return response.getPayload();
    }

    @Override
    public Page<OrderDetails> getEmployeeOrders(String employeeID, OrderStatus status, int pageNumber, int pageSize) 
        throws JsonProcessingException {
        log.info("getEmployeeOrders(employeeID={}, status={}, pageNumber={}, pageSize={})", 
            employeeID, status, pageNumber, pageSize);
        // create a DTO for order
        OrderDTO orderDTO = new OrderDTO(status, pageNumber, pageSize);
        orderDTO.setEmployeeID(employeeID);
        // create a DTO message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetOrdersByEmployeeID);
        eventMessage.setPayload(orderDTO);
        // receive data
        TypeReference<Page<OrderDetails>> typeReference = new TypeReference<Page<OrderDetails>>() {};
        EventMessage<Page<OrderDetails>> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ORDER_QUEUE, 
            MessageQueueConstant.ORDER_ROUTING_KEY,
            typeReference);
        log.info("response={}", response);
        return  (Page<OrderDetails>) response.getPayload();
    }
    
}
