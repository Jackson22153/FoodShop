package com.phucx.order.service.shipper;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.EventType;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.model.DataRequest;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.Shipper;
import com.phucx.order.model.UserRequest;
import com.phucx.order.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShipperServiceImp implements ShipperService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public Shipper getShipper(Integer shipperID) throws JsonProcessingException {
        log.info("getShipper(shipperID={})", shipperID);
        // create a request for user
        UserRequest userRequest = new UserRequest();
        userRequest.setShipperID(shipperID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetShipperByID);
        eventMessage.setPayload(userRequest);
        // receive data
        return this.sendAndReceiveData(eventMessage);
    }

    // send and receive shipper from user queue
    private Shipper sendAndReceiveData(EventMessage<DataRequest> eventMessage) throws JsonProcessingException{
        EventMessage<Shipper> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.USER_QUEUE, 
            MessageQueueConstant.USER_ROUTING_KEY,
            Shipper.class);
        log.info("response={}", response);
        return response.getPayload();
    }
    
}
