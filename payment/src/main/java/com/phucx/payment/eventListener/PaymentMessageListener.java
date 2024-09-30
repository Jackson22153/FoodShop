package com.phucx.payment.eventListener;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.base.rest.PayPalRESTException;
import com.phucx.payment.config.MessageQueueConfig;
import com.phucx.payment.constant.EventType;
import com.phucx.payment.constant.PaymentStatusConstant;
import com.phucx.payment.exception.PaymentNotFoundException;
import com.phucx.payment.model.EventMessage;
import com.phucx.payment.model.PaymentDTO;
import com.phucx.payment.model.PaymentResponse;
import com.phucx.payment.model.ResponseFormat;
import com.phucx.payment.service.payment.PaymentManagementService;
import com.phucx.payment.service.payment.PaymentProcessorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.PAYMENT_QUEUE)
public class PaymentMessageListener {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PaymentProcessorService paymentProcessorService;
    @Autowired
    private PaymentManagementService paymentManagementService;
    
    @RabbitHandler    
    public String paymenthandler(String message){
        log.info("paymenthandler(message={})", message);
        String eventID = UUID.randomUUID().toString();
        EventMessage<Object> response = new EventMessage<>();
        response.setEventId(eventID);
        try {
            TypeReference<EventMessage<PaymentDTO>> typeReference = new TypeReference<EventMessage<PaymentDTO>>() {};
            EventMessage<PaymentDTO> eventMessage = objectMapper.readValue(message, typeReference);
            PaymentDTO paymentDTO = eventMessage.getPayload();
            // handle event
            switch (eventMessage.getEventType()) {
                case CreatePayment:
                    response = createPayment(paymentDTO, response);
                    break;
                case UpdatePaymentStatusAsSuccessfulByID:
                    response = updatePaymentStatusAsSuccessful(paymentDTO.getPaymentID(), response);
                    break;
                case UpdatePaymentStatusAsCanceledByID:
                    response = updatePaymentStatusAsCanceled(paymentDTO.getPaymentID(), response);
                    break;
                case UpdatePaymentStatusAsSuccessfulByOrderID:
                    response = updatePaymentStatusByOrderIDAsSuccessful(paymentDTO.getOrderID(), response);
                    break;
                case UpdatePaymentStatusAsCanceledByOrderID:
                    response = updatePaymentStatusByOrderIDAsCanceled(paymentDTO.getOrderID(), response);
                    break;
                
                
                default:
                    break;
            }

            String responseMessage = objectMapper.writeValueAsString(response);
            return responseMessage;
        } catch (PayPalRESTException e) {
            log.error("PaypalRestException: {}", e.getMessage());
            return paypalExceptionHandler(response, e.getMessage());
        } catch(JsonProcessingException e){
            log.error("Error: {}", e.getMessage());
            return null;
        } catch (PaymentNotFoundException e){
            log.error("PaymentNotFoundException", e.getMessage());
            return paymentNotFoundExceptionHandler(response, e.getMessage());
        }
    }

    // create payment
    private EventMessage<Object> createPayment(PaymentDTO paymentDTO, EventMessage<Object> response) throws PayPalRESTException{
        PaymentResponse paymentResponse = paymentProcessorService.createPayment(paymentDTO);
        response.setEventType(EventType.ReturnStatusCreatePayment);
        response.setPayload(paymentResponse);
        return response;
    }


    // update payment by orderid as successful
    private EventMessage<Object> updatePaymentStatusByOrderIDAsSuccessful(String orderID, EventMessage<Object> response) 
        throws PaymentNotFoundException{
        Boolean result = this.paymentManagementService.updatePaymentAsSuccessfulByOrderIDPerMethod(orderID);
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setStatus(result);
        response.setPayload(responseFormat);
        response.setEventType(EventType.ReturnStatusUpdatePaymentStatusAsSuccessfulByID);
        return response;    
    }

    // update payment by orderid as canceled
    private EventMessage<Object> updatePaymentStatusByOrderIDAsCanceled(String orderID, EventMessage<Object> response) 
        throws PaymentNotFoundException{
        Boolean result = this.paymentManagementService.updatePaymentAsCanceledByOrderIDPerMethod(orderID);
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setStatus(result);
        response.setPayload(responseFormat);
        response.setEventType(EventType.ReturnStatusUpdatePaymentStatusAsCanceledByID);
        return response;    
    }



    // update payment as successful
    private EventMessage<Object> updatePaymentStatusAsSuccessful(String paymentID, EventMessage<Object> response) throws PaymentNotFoundException{
        response = this.updatePaymentStatus(paymentID, PaymentStatusConstant.SUCCESSFUL, response);
        response.setEventType(EventType.ReturnStatusUpdatePaymentStatusAsSuccessfulByID);
        return response;    
    }

    // update payment as canceled
    private EventMessage<Object> updatePaymentStatusAsCanceled(String paymentID, EventMessage<Object> response) throws PaymentNotFoundException{
        response = this.updatePaymentStatus(paymentID, PaymentStatusConstant.CANCELLED, response);
        response.setEventType(EventType.ReturnStatusUpdatePaymentStatusAsCanceledByID);
        return response;    
    }

    // update payment status
    private EventMessage<Object> updatePaymentStatus(String paymentID, PaymentStatusConstant status, EventMessage<Object> response) throws PaymentNotFoundException{
        Boolean result = paymentManagementService.updatePaymentStatus(paymentID, status.name().toLowerCase());
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setStatus(result);
        response.setPayload(responseFormat);
        return response;    
    }

    // error handler
    private String paypalExceptionHandler(EventMessage<Object> response, String message){
        try {
            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setStatus(false);
            paymentResponse.setMessage(message);

            return objectMapper.writeValueAsString(paymentResponse);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
    }

    private String paymentNotFoundExceptionHandler(EventMessage<Object> response, String message){
        try {
            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setStatus(false);
            paymentResponse.setMessage(message);
            return objectMapper.writeValueAsString(paymentResponse);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
    }

}
