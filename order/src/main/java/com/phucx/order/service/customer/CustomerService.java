package com.phucx.order.service.customer;

import java.sql.SQLException;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.CustomerAccount;
import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderDetailsDTO;
import com.phucx.order.model.OrderWithProducts;

import jakarta.ws.rs.NotFoundException;

public interface CustomerService {
    // customer info
    public Customer getCustomerByID(String customerID);
    public CustomerAccount getCustomerByUsername(String username);
    // place an order by customer
    public OrderDetailsDTO placeOrder(OrderWithProducts order, Authentication authentication) 
        throws InvalidDiscountException, InvalidOrderException, NotFoundException, SQLException, RuntimeException;
    public Notification receiveOrder(OrderWithProducts order);
    // notification
    Page<Notification> getNotifications(String userID, int pageNumber, int pageSize);
    Boolean turnOffNotification(String notificationID, String userID);
}
