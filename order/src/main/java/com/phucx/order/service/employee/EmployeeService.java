package com.phucx.order.service.employee;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.model.Employee;
import com.phucx.order.model.Notification;

public interface EmployeeService {
    // get employee
    public Employee getEmployeeByID(String employeeID) throws JsonProcessingException;
    // public EmployeeAccount getEmployeeAccount(String username);
    // processing order of customer
    // public Notification confirmOrder(OrderWithProducts order, String employeeID) throws InvalidOrderException;
    // public Notification cancelOrder(OrderWithProducts order, String employeeID);
    // public Notification fulfillOrder(OrderWithProducts order);
    // notification
    public Page<Notification> getNotifications(String userID, int pageNumber, int pageSize);
    Boolean turnOffNotification(String notificationID, String userID);
}
