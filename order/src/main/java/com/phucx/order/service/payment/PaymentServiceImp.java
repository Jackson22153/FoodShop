package com.phucx.order.service.payment;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.EventType;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.model.DataDTO;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.PaymentDTO;
import com.phucx.order.model.PaymentResponse;
import com.phucx.order.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentServiceImp implements PaymentService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public PaymentResponse createPayment(PaymentDTO paymentDTO) throws JsonProcessingException {
        log.info("createPayment(paymentDTO={})", paymentDTO);

        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        String eventID = UUID.randomUUID().toString();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.CreatePayment);
        eventMessage.setPayload(paymentDTO);        
        // receive response
        EventMessage<PaymentResponse> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PAYMENT_EXCHANGE, MessageQueueConstant.PAYMENT_ROUTING_KEY, 
            PaymentResponse.class);
            
        log.info("response: {}", response);
        return response.getPayload();
    }

    @Override
    public PaymentResponse updatePaymentByOrderIDAsSuccesful(String orderID) throws JsonProcessingException {
        log.info("updatePaymentByOrderIDAsSuccesful(orderID={})", orderID);
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        String eventID = UUID.randomUUID().toString();
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setOrderID(orderID);
        // set event message
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.UpdatePaymentStatusAsSuccessfulByOrderID);
        eventMessage.setPayload(paymentDTO);        
        // receive response
        EventMessage<PaymentResponse> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PAYMENT_EXCHANGE, MessageQueueConstant.PAYMENT_ROUTING_KEY, 
            PaymentResponse.class);
            
        log.info("response: {}", response);
        return response.getPayload();
    }

    @Override
    public PaymentResponse updatePaymentByOrderIDAsCanceled(String orderID) throws JsonProcessingException {
        log.info("updatePaymentByOrderIDAsCanceled(orderID={})", orderID);
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        String eventID = UUID.randomUUID().toString();
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setOrderID(orderID);
        // set event message
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.UpdatePaymentStatusAsCanceledByOrderID);
        eventMessage.setPayload(paymentDTO);        
        // receive response
        EventMessage<PaymentResponse> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PAYMENT_EXCHANGE, MessageQueueConstant.PAYMENT_ROUTING_KEY, 
            PaymentResponse.class);
            
        log.info("response: {}", response);
        return response.getPayload();
    }
    
}
