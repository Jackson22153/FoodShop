package com.phucx.shop.messageQueueListener;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.constant.EventType;
import com.phucx.converter.ProductDiscountsDTOConverter;
import com.phucx.model.EventMessage;
import com.phucx.model.ProductDiscountsDTO;
import com.phucx.shop.config.MessageQueueConfig;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.DiscountDetail;
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
            TypeReference<EventMessage<LinkedHashMap<String, Object>>> typeRef = 
                new TypeReference<EventMessage<LinkedHashMap<String, Object>>>() {};
            EventMessage<LinkedHashMap<String, Object>> eventMessage = 
                objectMapper.readValue(message, typeRef);
            LinkedHashMap<String, Object> payload = eventMessage.getPayload();
            if(eventMessage.getEventType().equals(EventType.GetDiscountByID)){
                // get discount by id
                String discountID = payload.get("discountID").toString();
                DiscountDetail discount = discountService.getDiscountDetail(discountID);
                // set response message
                responseMessage.setEventType(EventType.ReturnDiscountByID);
                responseMessage.setPayload(discount);
            }else if(eventMessage.getEventType().equals(EventType.GetDiscountsByIDs)){
                // get products by ids
                List<String> discountIDs = (List<String>) payload.get("discountIDs");
                List<DiscountDetail> discountDetails = discountService.getDiscountDetails(discountIDs);
                // set response message
                responseMessage.setEventType(EventType.ReturnProductByID);
                responseMessage.setPayload(discountDetails);
            }else if(eventMessage.getEventType().equals(EventType.ValidateDiscounts)){
                // validate discount
                List<LinkedHashMap<String, Object>> productDiscounts = 
                    (List<LinkedHashMap<String, Object>>) payload.get("productsDiscounts");
                List<ProductDiscountsDTO> products = ProductDiscountsDTOConverter
                    .castProductDiscountDTOs(productDiscounts);
                ResponseFormat responseFormat = validateDiscountService
                    .validateDiscountsOfProducts(products);
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
