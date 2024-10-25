package com.phucx.notification.service.notification;

import com.phucx.model.UserNotificationDTO;

public interface SendUserNotificationService {
    public void sendNotificationToCustomer(UserNotificationDTO userNotificationDTO);
    public void sendNotificationToEmployee(UserNotificationDTO userNotificationDTO);
    public void sendNotificationToAllEmployees(UserNotificationDTO userNotificationDTO);
    public void sendNotificationToAllCustomers(UserNotificationDTO userNotificationDTO);
}
