package com.phucx.order.service.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.model.OrderNotificationDTO;

public interface NotificationService {
    // send notification to notification service
    public void sendCustomerOrderNotification(OrderNotificationDTO notification) throws JsonProcessingException;
    public void sendEmployeeOrderNotification(OrderNotificationDTO notification) throws JsonProcessingException;
    // mark pending order as confirmed for all employees
    public void markAsReadForConfirmedOrderNotification(OrderNotificationDTO notification) throws JsonProcessingException;
}
