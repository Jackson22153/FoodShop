package com.phucx.notification.service.notification;

import com.phucx.notification.exception.NotFoundException;

public interface MarkUserNotificationService {
    // mark as read
    // employee
    // mark notifications as read
    public Boolean markAsReadEmployeeNotification(String notificationID, String userID) throws NotFoundException;
    // mark notifications as read
    public Boolean markAsReadEmployeeNotifications(String userID);
    // mark notification as read for broadcast
    public Boolean markAsReadEmployeeBroadcastNotification(String notifiationID) throws NotFoundException;

    public Boolean markAsReadForEmployee(String notificationID, String userID, String markType) throws NotFoundException;
    // customer
    // mark notifications as read
    public Boolean markAsReadCustomerNotification(String notificationID, String userID) throws NotFoundException;
    // mark notifications as read
    public Boolean markAsReadCustomerNotifications(String userID);
    // mark notification as read for broadcast
    public Boolean markAsReadCustomerBroadcastNotification(String notifiationID) throws NotFoundException;

    public Boolean markAsReadForCustomer(String notificationID, String userID, String markType) throws NotFoundException;

    public Boolean markAsReadForNotification(String notificationID);

}
