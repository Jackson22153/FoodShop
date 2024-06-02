package com.phucx.account.eventListener;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.constant.EventType;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.Shipper;
import com.phucx.account.model.ShipperDTO;
import com.phucx.account.service.shipper.ShipperService;

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
        String eventID = UUID.randomUUID().toString();
        EventMessage<Object> responseMessage = new EventMessage<>();
        responseMessage.setEventId(eventID);
        try {
            TypeReference<EventMessage<ShipperDTO>> typeRef = new TypeReference<EventMessage<ShipperDTO>>() {};
            EventMessage<ShipperDTO> shipperDTO = objectMapper.readValue(message, typeRef);
            ShipperDTO payload = shipperDTO.getPayload();
            // fetch data
            if(shipperDTO.getEventType().equals(EventType.GetShipperByID)){
                // get shipper by id
                Integer shipperID = payload.getShipperID();
                Shipper fetchedShipper = shipperService.getShipperByID(shipperID);
                // set response message
                responseMessage.setPayload(fetchedShipper);
                responseMessage.setEventType(EventType.ReturnShipperByID);
            }
            String response = objectMapper.writeValueAsString(responseMessage);
            return response;
        } catch (Exception e) {
           log.error("Error: {}", e.getMessage());
           return null;
        }
    }
}
