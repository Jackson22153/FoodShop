package com.phucx.notification.service.notification;


import java.util.List;

import org.springframework.data.domain.Page;
import javax.naming.NameNotFoundException;

import com.phucx.notification.constant.NotificationBroadCast;
import com.phucx.notification.model.NotificationDetail;

public interface NotificationService {
    // update/ create notification
    public NotificationDetail createNotification(NotificationDetail notification);    
    public Boolean updateNotificationReadStatusByNotificationID(String notificationID, Boolean status) throws NameNotFoundException;
    public Boolean updateNotificationReadStatusByUserID(String userID, NotificationBroadCast broadCast, Boolean status);
    // get notification
    public NotificationDetail getNotificationByUserIDAndNotificationID(String userID, String notificationID) throws NameNotFoundException;
    public NotificationDetail getNotificationByUserIDOrBroadCastAndNotificationID(String userID, NotificationBroadCast broadCast, String notificationID) throws NameNotFoundException;
    public NotificationDetail getNotificationsByID(String notificationID) throws NameNotFoundException;

    public Page<NotificationDetail> getNotificationsByTopicName(String topicName, int pageNumber, int pageSize);
    public List<NotificationDetail> getNotificationsByTopicName(String topicName);
    
    public Page<NotificationDetail> getNotificationsByReceiverID(String userID, int pageNumber, int pageSize);
    public Page<NotificationDetail> getNotificationsByReceiverIDOrBroadCast(String userID, NotificationBroadCast broadCast, int pageNumber, int pageSize);
    public Page<NotificationDetail> getNotificationsByReceiverIDAndTopicName(String userID, NotificationBroadCast broadCast, String topicName, int pageNumber, int pageSize);

    // mark as read
    public Boolean markAsReadEmployeeNotification(String notificationID, String userID) throws NameNotFoundException;
    public Boolean markAsReadCustomerNotification(String notificationID, String userID) throws NameNotFoundException;
    public Boolean markAsReadEmployeeNotifications(String userID);
    public Boolean markAsReadCustomerNotifications(String userID);

    // get number of notifications
    public Long getUserTotalNumberOfUnreadNotifications(String userID, NotificationBroadCast broadCast);
}
