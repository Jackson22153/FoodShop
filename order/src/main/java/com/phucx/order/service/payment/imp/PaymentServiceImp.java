package com.phucx.order.service.payment.imp;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.constant.EventType;
import com.phucx.model.DataDTO;
import com.phucx.model.EventMessage;
import com.phucx.model.PaymentDTO;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.model.PaymentDetails;
import com.phucx.order.model.PaymentResponse;
import com.phucx.order.service.messageQueue.MessageQueueService;
import com.phucx.order.service.payment.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentServiceImp implements PaymentService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public PaymentResponse createPayment(PaymentDTO paymentDTO) throws JsonProcessingException {
        log.info("createPayment(paymentDTO={})", paymentDTO);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.CreatePayment);
        eventMessage.setPayload(paymentDTO);
        // receive data
        EventMessage<PaymentResponse> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PAYMENT_EXCHANGE, 
            MessageQueueConstant.PAYMENT_ROUTING_KEY,
            PaymentResponse.class);
        log.info("response={}", response);
        return  response.getPayload();
    }

    @Override
    public PaymentDetails getPayment(String orderID) throws JsonProcessingException {
        log.info("getPayment(orderID={})", orderID);
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setOrderID(orderID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetPaymentDetailsByOrderID);
        eventMessage.setPayload(paymentDTO);
        // receive data
        EventMessage<PaymentDetails> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PAYMENT_EXCHANGE, 
            MessageQueueConstant.PAYMENT_ROUTING_KEY,
            PaymentDetails.class);
        log.info("response={}", response);
        return  response.getPayload();
    }

    @Override
    public PaymentResponse updatePaymentByOrderIDAsSuccesful(String orderID) throws JsonProcessingException {
        log.info("updatePaymentByOrderIDAsSuccesful(orderID={})", orderID);
        // create dto
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setOrderID(orderID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.UpdatePaymentByOrderIDAsSuccesful);
        eventMessage.setPayload(paymentDTO);
        // receive data
        EventMessage<PaymentResponse> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PAYMENT_EXCHANGE, 
            MessageQueueConstant.PAYMENT_ROUTING_KEY,
            PaymentResponse.class);
        log.info("response={}", response);
        return  response.getPayload();
    }

    @Override
    public PaymentResponse updatePaymentByOrderIDAsCanceled(String orderID) throws JsonProcessingException {
        log.info("updatePaymentByOrderIDAsCanceled(orderID={})", orderID);
        // create dto
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setOrderID(orderID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.UpdateOrderStatusAsCanceled);
        eventMessage.setPayload(paymentDTO);
        // receive data
        EventMessage<PaymentResponse> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PAYMENT_EXCHANGE, 
            MessageQueueConstant.PAYMENT_ROUTING_KEY,
            PaymentResponse.class);
        log.info("response={}", response);
        return  response.getPayload();
    }
    
}
