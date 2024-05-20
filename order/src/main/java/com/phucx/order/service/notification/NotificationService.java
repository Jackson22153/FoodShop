package com.phucx.order.service.notification;


import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.order.model.Notification;

public interface NotificationService {
    // update/ create notification
    public Notification createNotification(Notification notification);    
    public Boolean updateNotificationActive(String notificationID, Boolean status);
    // get notification
    public Notification getNotificationByUserIDAndNotificationID(String userID, String notificationID);
    public Notification getNotificationByUserIDOrNullAndNotificationID(String userID, String notificationID);
    public Notification getNotificationsByID(String notificationID);
    public Page<Notification> getNotificationsByTopicName(String topicName, int pageNumber, int pageSize);
    public List<Notification> getNotificationsByTopicName(String topicName);
    public Page<Notification> getNotificationsByReceiverID(String userID, int pageNumber, int pageSize);
    public Page<Notification> getNotificationsByReceiverIDOrNull(String userID, int pageNumber, int pageSize);
    public Page<Notification> getNotificationsByReceiverIDAndTopicName(String userID, String topicName, int pageNumber, int pageSize);
}
