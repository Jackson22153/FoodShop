package com.phucx.account.eventListener;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.exception.ShipperNotFoundException;
import com.phucx.account.model.Shipper;
import com.phucx.account.service.shipper.ShipperService;
import com.phucx.constant.EventType;
import com.phucx.model.EventMessage;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.SHIPPER_QUEUE)
public class ShipperMessageListener {
    @Autowired
    private ShipperService shipperService;
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitHandler
    public String fetchShipper(String message){
        log.info("fetchShipper({})", message);
        // create response message
        EventMessage<Object> responseMessage = this.createResponseMessage(Object.class);
        try {
            TypeReference<EventMessage<LinkedHashMap<String, Object>>> typeRef = 
                new TypeReference<EventMessage<LinkedHashMap<String, Object>>>() {};
            EventMessage<LinkedHashMap<String, Object>> shipperDTO = objectMapper.readValue(message, typeRef);
            LinkedHashMap<String, Object> payload = shipperDTO.getPayload();
            // fetch data
            if(shipperDTO.getEventType().equals(EventType.GetShipperByID)){
                // get shipper by id
                Integer shipperID = (Integer) payload.get("shipperID");
                Shipper fetchedShipper = shipperService.getShipperByID(shipperID);
                // set response message
                responseMessage.setPayload(fetchedShipper);
                responseMessage.setEventType(EventType.ReturnShipperByID);
            }
            String response = objectMapper.writeValueAsString(responseMessage);
            return response;
        } catch (ShipperNotFoundException e){
            log.error("Error: {}", e.getMessage());
            try {
                responseMessage.setErrorMessage(e.getMessage());
                responseMessage.setEventType(EventType.NotFoundException);
                String responsemessage = objectMapper.writeValueAsString(responseMessage);
                return responsemessage; 
            } catch (Exception exception) {
                log.error("Error: {}", e.getMessage());
                return null;
            }
        } catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
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
