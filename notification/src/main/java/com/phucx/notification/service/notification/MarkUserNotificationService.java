package com.phucx.notification.service.notification;

import javax.naming.NameNotFoundException;

public interface MarkUserNotificationService {
    // mark as read
    // employee
    // mark notifications as read
    public Boolean markAsReadEmployeeNotification(String notificationID, String userID) throws NameNotFoundException;
    // mark notifications as read
    public Boolean markAsReadEmployeeNotifications(String userID);
    // mark notification as read for broadcast
    public Boolean markAsReadEmployeeBroadcastNotification(String notifiationID) throws NameNotFoundException;

    public Boolean markAsReadForEmployee(String notificationID, String userID, String markType) throws NameNotFoundException;
    // customer
    // mark notifications as read
    public Boolean markAsReadCustomerNotification(String notificationID, String userID) throws NameNotFoundException;
    // mark notifications as read
    public Boolean markAsReadCustomerNotifications(String userID);
    // mark notification as read for broadcast
    public Boolean markAsReadCustomerBroadcastNotification(String notifiationID) throws NameNotFoundException;

    public Boolean markAsReadForCustomer(String notificationID, String userID, String markType) throws NameNotFoundException;

}
