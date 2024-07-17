package com.phucx.shop.messageQueueListener;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.shop.config.MessageQueueConfig;
import com.phucx.shop.constant.EventType;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.DiscountDetail;
import com.phucx.shop.model.DiscountDTO;
import com.phucx.shop.model.EventMessage;
import com.phucx.shop.model.ProductDiscountsDTO;
import com.phucx.shop.model.ResponseFormat;
import com.phucx.shop.service.discount.DiscountService;
import com.phucx.shop.service.discount.ValidateDiscountService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.DISCOUNT_QUEUE)
public class DiscountMessageListener {
    @Autowired
    private DiscountService discountService;
    @Autowired
    private ValidateDiscountService validateDiscountService;
    @Autowired
    private ObjectMapper objectMapper;
    // get discount
    @RabbitHandler
    public String fetchDiscount(String message){
        log.info("fetchDiscount({})", message);
        EventMessage<Object> responseMessage = this.createResponseMessage(Object.class);
        try {
            TypeReference<EventMessage<DiscountDTO>> typeRef = new TypeReference<EventMessage<DiscountDTO>>() {};
            EventMessage<DiscountDTO> eventMessage = objectMapper.readValue(message, typeRef);
            DiscountDTO payload = eventMessage.getPayload();
            if(eventMessage.getEventType().equals(EventType.GetDiscountByID)){
                // get discount by id
                String discountID = payload.getDiscountID();
                DiscountDetail discount = discountService.getDiscountDetail(discountID);
                // set response message
                responseMessage.setEventType(EventType.ReturnDiscountByID);
                responseMessage.setPayload(discount);
            }else if(eventMessage.getEventType().equals(EventType.GetDiscountsByIDs)){
                // get products by ids
                List<String> discountIDs = payload.getDiscountIDs();
                List<DiscountDetail> discountDetails = discountService.getDiscountDetails(discountIDs);
                // set response message
                responseMessage.setEventType(EventType.ReturnProductByID);
                responseMessage.setPayload(discountDetails);
            }else if(eventMessage.getEventType().equals(EventType.ValidateDiscounts)){
                // validate discount
                List<ProductDiscountsDTO> productDiscounts = payload.getProductsDiscounts();
                ResponseFormat responseFormat = validateDiscountService.validateDiscountsOfProducts(productDiscounts);
                responseMessage.setPayload(responseFormat);
            }
            String response = objectMapper.writeValueAsString(responseMessage);
            return response;
        } catch (NotFoundException e){
            log.error("Error: {}", e.getMessage());
            return handleNotFoundException(responseMessage, e.getMessage());
        }catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
    }

    private String handleNotFoundException(EventMessage<Object> responseMessage, String errorMessage){
        try {
            responseMessage.setErrorMessage(errorMessage);
            responseMessage.setEventType(EventType.NotFoundException);
            String responsemessage = objectMapper.writeValueAsString(responseMessage);
            return responsemessage;
        } catch (Exception exception) {
            log.error("Error: {}", exception.getMessage());
            return null;
        }
    }

    private <T> EventMessage<T> createResponseMessage(Class<T> type){
        EventMessage<T> responseMessage = new EventMessage<>();
        String eventID = UUID.randomUUID().toString();
        responseMessage.setEventId(eventID);
        return responseMessage;
    }
}
