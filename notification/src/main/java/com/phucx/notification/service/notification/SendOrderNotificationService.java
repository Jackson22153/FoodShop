package com.phucx.notification.service.notification;

import com.phucx.model.OrderNotificationDTO;
import com.phucx.notification.exception.NotFoundException;

public interface SendOrderNotificationService {
    // customer send notification message to employee
    public void sendNotificationToEmployee(OrderNotificationDTO notificationDetail) throws NotFoundException;
    public void sendNotificationToAllEmployees(OrderNotificationDTO notificationDetail);
    // employee send notification message to customer
    public void sendNotificationToCustomer(OrderNotificationDTO notificationDetail) throws NotFoundException;
    public void sendNotificationToAllCustomers(OrderNotificationDTO notificationDetail);
}
