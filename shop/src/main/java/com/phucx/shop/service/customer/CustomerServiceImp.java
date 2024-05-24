package com.phucx.shop.service.customer;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.shop.constant.EventType;
import com.phucx.shop.model.Customer;
import com.phucx.shop.model.EventMessage;
import com.phucx.shop.service.messageQueue.MessageQueueService;

import jakarta.ws.rs.NotFoundException;

@Service
public class CustomerServiceImp implements CustomerService{
    @Autowired
    private MessageQueueService messageQueueService;
 
    @Override
    public Customer getCustomerByUserID(String userID) {
        Customer customer = new Customer();
        customer.setUserID(userID);
        String eventID = UUID.randomUUID().toString();
        EventMessage eventMessage = new EventMessage(eventID, EventType.GetCustomerByUserID, customer);
        Customer fetchedCustomer = (Customer) messageQueueService.sendAndReceiveData(eventMessage);
        if(fetchedCustomer==null) throw new NotFoundException("Customer with userID " + userID + " does not found");
        return fetchedCustomer;
    }
    
}
