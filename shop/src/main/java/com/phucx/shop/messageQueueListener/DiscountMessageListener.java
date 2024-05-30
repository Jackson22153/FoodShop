package com.phucx.shop.messageQueueListener;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.shop.config.MessageQueueConfig;
import com.phucx.shop.constant.EventType;
import com.phucx.shop.model.DiscountDetail;
import com.phucx.shop.model.DiscountRequest;
import com.phucx.shop.model.EventMessage;
import com.phucx.shop.model.ProductDiscountsDTO;
import com.phucx.shop.model.ResponseFormat;
import com.phucx.shop.service.discount.DiscountService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.DISCOUNT_QUEUE)
public class DiscountMessageListener {
    @Autowired
    private DiscountService discountService;
    @Autowired
    private ObjectMapper objectMapper;
    // get discount
    @RabbitHandler
    public String fetchDiscount(EventMessage<DiscountRequest> eventMessage){
        log.info("fetchDiscount({})", eventMessage);
        String eventID = UUID.randomUUID().toString();
        EventMessage<Object> responseMessage = new EventMessage<>();
        responseMessage.setEventId(eventID);
        DiscountRequest payload = eventMessage.getPayload();
        try {
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
                Boolean status = discountService.validateDiscountsOfProducts(productDiscounts);
                responseMessage.setPayload(new ResponseFormat(status));
            }
            String response = objectMapper.writeValueAsString(responseMessage);
            return response;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
    }
}
