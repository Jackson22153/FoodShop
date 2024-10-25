package com.phucx.account.service.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.model.UserNotificationDTO;

public interface NotificationService {
    public void sendCustomerNotification(UserNotificationDTO userNotification) throws JsonProcessingException;
    public void sendEmployeeNotification(UserNotificationDTO userNotification) throws JsonProcessingException;
}
