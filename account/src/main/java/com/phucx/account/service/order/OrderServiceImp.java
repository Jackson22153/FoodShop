package com.phucx.account.service.order;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.phucx.account.constant.EventType;
import com.phucx.account.constant.MessageQueueConstant;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.InvoiceDTO;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderRequest;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImp implements OrderService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    @SuppressWarnings("unchecked")
    public Page<OrderDetailsDTO> getCustomerOrders(String customerID, OrderStatus orderStatus, int pageNumber, int pageSize) {
        log.info("getCustomerOrders(customerID={}, orderStatus={}, pageNumber={}, pageSize={})", 
            customerID, orderStatus, pageNumber, pageSize);
        // create a order request
        OrderRequest orderRequest = new OrderRequest(orderStatus, pageNumber, pageSize);
        orderRequest.setCustomerID(customerID);
        // create a event message for order
        String eventID = UUID.randomUUID().toString();
        EventMessage<OrderRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetOrdersByCustomerID);
        eventMessage.setPayload(orderRequest);
        // receive data
        EventMessage<Object> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ORDER_QUEUE, 
            MessageQueueConstant.ORDER_ROUTING_KEY);
        log.info("response={}", response);
        return (Page<OrderDetailsDTO>) response.getPayload();
    }

    @Override
    public InvoiceDTO getCustomerInvoice(String orderID, String customerID) {
        log.info("getCustomerInvoice(orderID={}, customerID={})", orderID, customerID);
        // create a request for order
        OrderRequest orderRequest = new OrderRequest(orderID);
        orderRequest.setCustomerID(customerID);
        String eventID = UUID.randomUUID().toString();
        EventMessage<OrderRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetOrderInvoiceByIdAndCustomerID);
        eventMessage.setPayload(orderRequest);
        // receive data
        EventMessage<Object> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ORDER_QUEUE, 
            MessageQueueConstant.ORDER_ROUTING_KEY);
        log.info("response={}", response);
        return (InvoiceDTO) response.getPayload();
    }

    @Override
    public OrderWithProducts getEmployeeOrder(String orderID, String employeeID) {
        log.info("getEmployeeOrder(orderID={}, employeeID={})", orderID, employeeID);
        // create a request for order
        OrderRequest orderRequest = new OrderRequest(orderID);
        orderRequest.setEmployeeID(employeeID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<OrderRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetOrderByEmployeeID);
        eventMessage.setPayload(orderRequest);
        // receive data
        EventMessage<Object> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ORDER_QUEUE, 
            MessageQueueConstant.ORDER_ROUTING_KEY);
        log.info("response={}", response);
        return (OrderWithProducts) response.getPayload();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<OrderDetailsDTO> getEmployeeOrders(String employeeID, OrderStatus status, int pageNumber, int pageSize) {
        log.info("getEmployeeOrders(employeeID={}, status={}, pageNumber={}, pageSize={})", employeeID, status, pageNumber, pageSize);
        // create a request for order
        OrderRequest orderRequest = new OrderRequest(status, pageNumber, pageSize);
        orderRequest.setEmployeeID(employeeID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<OrderRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetOrdersByEmployeeID);
        eventMessage.setPayload(orderRequest);
        // receive data
        EventMessage<Object> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ORDER_QUEUE, 
            MessageQueueConstant.ORDER_ROUTING_KEY);
        log.info("response={}", response);
        return  (Page<OrderDetailsDTO>) response.getPayload();
    }
    
}
