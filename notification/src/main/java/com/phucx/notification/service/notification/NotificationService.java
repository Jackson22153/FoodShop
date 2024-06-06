package com.phucx.notification.service.notification;


import java.util.List;

import org.springframework.data.domain.Page;
import javax.naming.NameNotFoundException;
import com.phucx.notification.model.Notification;

public interface NotificationService {
    // update/ create notification
    public Notification createNotification(Notification notification);    
    public Boolean updateNotificationActive(String notificationID, Boolean status) throws NameNotFoundException;
    // get notification
    public Notification getNotificationByUserIDAndNotificationID(String userID, String notificationID) throws NameNotFoundException;
    public Notification getNotificationByUserIDOrNullAndNotificationID(String userID, String notificationID) throws NameNotFoundException;
    public Notification getNotificationsByID(String notificationID) throws NameNotFoundException;

    public Page<Notification> getNotificationsByTopicName(String topicName, int pageNumber, int pageSize);
    public List<Notification> getNotificationsByTopicName(String topicName);
    
    public Page<Notification> getNotificationsByReceiverID(String userID, int pageNumber, int pageSize);
    public Page<Notification> getNotificationsByReceiverIDOrNull(String userID, int pageNumber, int pageSize);
    public Page<Notification> getNotificationsByReceiverIDAndTopicName(String userID, String topicName, int pageNumber, int pageSize);

    // mark as read
    public Boolean markAsReadEmployeeNotification(String notificationID, String userID) throws NameNotFoundException;
    public Boolean markAsReadCustomerNotification(String notificationID, String userID) throws NameNotFoundException;

    // get number of notifications
    public Long getEmployeeNumberOfNotifications(String userID);
    public Long getCustomerNumberOfNotifications(String userID);
}
