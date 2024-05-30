package com.phucx.order.service.customer;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.Notification;

public interface CustomerService {
    // customer info
    public Customer getCustomerByID(String customerID) throws JsonProcessingException;
    public List<Customer> getCustomersByIDs(List<String> customerIDs) throws JsonProcessingException;
    // public Customer getCustomerByUsername(String username);
    // place an order by customer
    // public OrderDetailDTO placeOrder(OrderWithProducts order, String customerID, String userID) 
    //     throws InvalidDiscountException, InvalidOrderException, NotFoundException, SQLException, RuntimeException;
    // public Notification receiveOrder(OrderWithProducts order);
    // notification
    Page<Notification> getNotifications(String userID, int pageNumber, int pageSize);
    Boolean turnOffNotification(String notificationID, String userID);
}
