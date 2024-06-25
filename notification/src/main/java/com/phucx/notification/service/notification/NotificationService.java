package com.phucx.notification.service.notification;


import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.notification.constant.NotificationBroadCast;
import com.phucx.notification.exception.NotFoundException;
import com.phucx.notification.model.NotificationDetail;

public interface NotificationService {
    // update/ create notification
    public NotificationDetail createNotification(NotificationDetail notification);    
    // update read status for specific notificationID and userID
    public Boolean updateNotificationReadStatusOfUser(String notificationID, String userID, Boolean status) throws NotFoundException;
    // update read status for userID
    public Boolean updateNotificationsReadStatus(String userID, Boolean status);
    // update read status for notification for all users
    public Boolean updateNotificationReadStatus(String notificationID, Boolean status);
    // update read status for broadcast notification for users
    public Boolean updateNotificationReadStatusOfBroadcast(String notificationID, NotificationBroadCast broadCast, Boolean status) throws NotFoundException;
    public Boolean updateNotificationReadStatusOfBroadcast(String title, String message, NotificationBroadCast broadCast, Boolean status) throws NotFoundException;
    
    // get notification

    public NotificationDetail getNotificationByNotificationIDAndReceiverID(String notificationID, String receiverID) throws NotFoundException;
    public NotificationDetail getNotificationByUserIDAndNotificationID(String userID, String notificationID) throws NotFoundException;
    public NotificationDetail getNotificationByUserIDOrBroadCastAndNotificationID(String userID, NotificationBroadCast broadCast, String notificationID) throws NotFoundException;
    public NotificationDetail getNotificationsByID(String notificationID) throws NotFoundException;
    // get notifications by topic
    public Page<NotificationDetail> getNotificationsByTopicName(String topicName, int pageNumber, int pageSize);
    public List<NotificationDetail> getNotificationsByTopicName(String topicName);
    
    public Page<NotificationDetail> getNotificationsByReceiverID(String userID, int pageNumber, int pageSize);
    public Page<NotificationDetail> getNotificationsByReceiverIDOrBroadCast(String userID, NotificationBroadCast broadCast, int pageNumber, int pageSize);
    public Page<NotificationDetail> getNotificationsByReceiverIDAndTopicName(String userID, NotificationBroadCast broadCast, String topicName, int pageNumber, int pageSize);

    // get number of notifications
    public Long getUserTotalNumberOfUnreadNotifications(String userID, NotificationBroadCast broadCast);
}
