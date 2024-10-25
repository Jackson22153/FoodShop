package com.phucx.order.service.shipper;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.constant.EventType;
import com.phucx.model.DataDTO;
import com.phucx.model.EventMessage;
import com.phucx.model.ShipperDTO;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.Shipper;
import com.phucx.order.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShipperServiceImp implements ShipperService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public Shipper getShipper(Integer shipperID) throws JsonProcessingException, NotFoundException {
        log.info("getShipper(shipperID={})", shipperID);
        // create a request for user
        ShipperDTO shipperDTO = new ShipperDTO();
        shipperDTO.setShipperID(shipperID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetShipperByID);
        eventMessage.setPayload(shipperDTO);
        // receive data
        return this.sendAndReceiveData(eventMessage);
    }

    // send and receive shipper from user queue
    private Shipper sendAndReceiveData(EventMessage<DataDTO> eventMessage) throws JsonProcessingException, NotFoundException{
        EventMessage<Shipper> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_EXCHANGE, 
            MessageQueueConstant.SHIPPER_ROUTING_KEY,
            Shipper.class);
        log.info("response={}", response);
        if(response.getEventType().equals(EventType.NotFoundException)){
            throw new NotFoundException(response.getErrorMessage());
        }
        return response.getPayload();
    }
    
}
