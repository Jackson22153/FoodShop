package com.phucx.order.service.discount;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.order.constant.EventType;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.DataDTO;
import com.phucx.order.model.DiscountDetail;
import com.phucx.order.model.DiscountDTO;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.ProductDiscountsDTO;
import com.phucx.order.model.ResponseFormat;
import com.phucx.order.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiscountServiceImp implements DiscountService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public DiscountDetail getDiscount(String discountID) throws JsonProcessingException, NotFoundException {
        log.info("getDiscount(discountID={})", discountID);
        // create a request for discount
        DiscountDTO discountDDiscountDTO = new DiscountDTO();
        discountDDiscountDTO.setDiscountID(discountID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetDiscountByID);
        eventMessage.setPayload(discountDDiscountDTO);
        // receive data
        EventMessage<DiscountDetail> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.SHOP_EXCHANGE, 
            MessageQueueConstant.DISCOUNT_ROUTING_KEY,
            DiscountDetail.class);
        log.info("response={}", response);
        if(response.getEventType().equals(EventType.NotFoundException)){
            throw new NotFoundException(response.getErrorMessage());
        }
        return response.getPayload();
    }

    @Override
    public List<DiscountDetail> getDiscounts(List<String> discountIDs) throws JsonProcessingException, NotFoundException {
        log.info("getDiscount(getDiscounts={})", discountIDs);
        // create a request for discount
        DiscountDTO discountDDiscountDTO = new DiscountDTO();
        discountDDiscountDTO.setDiscountIDs(discountIDs);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetDiscountsByIDs);
        eventMessage.setPayload(discountDDiscountDTO);
        // receive data
        TypeReference<EventMessage<List<DiscountDetail>>> typeReference = 
            new TypeReference<EventMessage<List<DiscountDetail>>>() {};
        EventMessage<List<DiscountDetail>> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.SHOP_EXCHANGE, 
            MessageQueueConstant.DISCOUNT_ROUTING_KEY, typeReference);
        log.info("response={}", response);
        if(response.getEventType().equals(EventType.NotFoundException)){
            throw new NotFoundException(response.getErrorMessage());
        }
        return response.getPayload();
    }

    @Override
    public Boolean validateDiscount(List<ProductDiscountsDTO> productsDiscounts) throws JsonProcessingException {
        log.info("validateDiscount(productDiscounts={})", productsDiscounts);
        // create a request for discount
        DiscountDTO discountDDiscountDTO = new DiscountDTO();
        discountDDiscountDTO.setProductsDiscounts(productsDiscounts);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.ValidateDiscounts);
        eventMessage.setPayload(discountDDiscountDTO);
        // receive data
        EventMessage<ResponseFormat> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.SHOP_EXCHANGE, 
            MessageQueueConstant.DISCOUNT_ROUTING_KEY, 
            ResponseFormat.class);
        log.info("response={}", response);
        return  response.getPayload().getStatus();
    }
}
