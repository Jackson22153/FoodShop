package com.phucx.order.service.customer;

import java.sql.SQLException;

import org.springframework.data.domain.Page;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderDetailDTO;
import com.phucx.order.model.OrderWithProducts;

import jakarta.ws.rs.NotFoundException;

public interface CustomerService {
    // customer info
    public Customer getCustomerByID(String customerID);
    // public Customer getCustomerByUsername(String username);
    // place an order by customer
    // public OrderDetailDTO placeOrder(OrderWithProducts order, String customerID, String userID) 
    //     throws InvalidDiscountException, InvalidOrderException, NotFoundException, SQLException, RuntimeException;
    // public Notification receiveOrder(OrderWithProducts order);
    // notification
    Page<Notification> getNotifications(String userID, int pageNumber, int pageSize);
    Boolean turnOffNotification(String notificationID, String userID);
}
