package com.phucx.notification.service.notification;

import java.util.List;
import java.util.UUID;
import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.notification.constant.NotificationBroadCast;
import com.phucx.notification.constant.NotificationIsRead;
import com.phucx.notification.model.NotificationDetail;
import com.phucx.notification.repository.NotificationDetailRepository;
import com.phucx.notification.repository.NotificationRepository;
import com.phucx.notification.repository.NotificationUserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationServiceImp implements NotificationService{
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationDetailRepository notificationDetailRepository;
    @Autowired
    private NotificationUserRepository notificationUserRepository;

    @Override
    public NotificationDetail createNotification(NotificationDetail notification) {
        log.info("createNotification({})", notification.toString());
        String notificationID = UUID.randomUUID().toString();
        notificationRepository.createNotification(
            notificationID, notification.getTitle(), 
            notification.getMessage(), notification.getSenderID(), 
            notification.getReceiverID(), notification.getTopic(), 
            notification.getStatus(), notification.getIsRead(), 
            notification.getTime());
        notification.setNotificationID(notificationID);
        return notification;
    }

    @Override
    public NotificationDetail getNotificationsByID(String notificationID) throws NameNotFoundException {
        return notificationDetailRepository.findById(notificationID)
            .orElseThrow(()-> new NameNotFoundException("Notification " + notificationID + " does not found"));
    }

    @Override
    public Page<NotificationDetail> getNotificationsByTopicName(String topicName, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return notificationDetailRepository.findByTopicName(topicName, pageable);
    }

    @Override
    public List<NotificationDetail> getNotificationsByTopicName(String topicName) {
        return notificationDetailRepository.findByTopicName(topicName);
    }

    @Override
    public Page<NotificationDetail> getNotificationsByReceiverID(String userID, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return notificationDetailRepository.findByReceiverIDOrderByTimeDesc(userID, pageable);
    }

    @Override
    public Boolean updateNotificationReadStatusByNotificationID(String notificationID, Boolean isRead) 
    throws NameNotFoundException {
        log.info("updateNotificationReadStatusByNotificationID(notificationID={}, isRead={})", notificationID, isRead);
        NotificationDetail notification = this.getNotificationsByID(notificationID);
        return notificationUserRepository.updateNotificationReadStatusByNotificationIDAndUserID(
            notification.getNotificationID(), notification.getReceiverID(), isRead);
    }

    @Override
    public Page<NotificationDetail> getNotificationsByReceiverIDAndTopicName(
        String userID, NotificationBroadCast broadCast, String topicName, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return notificationDetailRepository.findByTopicAndReceiverIDOrBroadCast(
            topicName, userID, broadCast.name(), pageable);
    }

    @Override
    public Page<NotificationDetail> getNotificationsByReceiverIDOrBroadCast(
        String userID, NotificationBroadCast broadCast, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return notificationDetailRepository.findByReceiverIDOrBroadCastOrderByTimeDesc(
            userID, broadCast.name(), pageable);
    }

    @Override
    public NotificationDetail getNotificationByUserIDAndNotificationID(String userID, String notificationID) throws NameNotFoundException {
        NotificationDetail notification = notificationDetailRepository.findByNotificationIDAndReceiverIDOrderByTimeDesc(notificationID, userID)
            .orElseThrow(()-> new NameNotFoundException("Notification " + notificationID + " of User " + userID + " does not found"));
        return notification;
    }

    @Override
    public NotificationDetail getNotificationByUserIDOrBroadCastAndNotificationID(String userID, NotificationBroadCast broadCast, String notificationID) 
    throws NameNotFoundException {
        NotificationDetail notification = notificationDetailRepository
        .findByNotificationIDAndReceiverIDOrBroadCastOrderByTimeDesc(notificationID, userID, broadCast.name())
        .orElseThrow(()-> new NameNotFoundException("Notification " + notificationID + " of User " + userID + " does not found"));
        return notification;
    }

    @Override
    public Boolean markAsReadEmployeeNotification(String notificationID, String userID) throws NameNotFoundException {
        log.info("markAsReadEmployeeNotification(notificationID={}, userID={})", notificationID, userID);
        NotificationDetail notification = this.getNotificationByUserIDOrBroadCastAndNotificationID(
            userID, NotificationBroadCast.ALL_EMPLOYEES, notificationID);
        return this.updateNotificationReadStatusByNotificationID(
            notification.getNotificationID(), NotificationIsRead.YES.getValue());
    }

    @Override
    public Boolean markAsReadCustomerNotification(String notificationID, String userID) throws NameNotFoundException {
        log.info("markAsReadCustomerNotification(notificationID={}, userID={})", notificationID, userID);
        NotificationDetail notification = this.getNotificationByUserIDOrBroadCastAndNotificationID(
            userID, NotificationBroadCast.ALL_CUSTOMERS, notificationID);
        return this.updateNotificationReadStatusByNotificationID(
            notification.getNotificationID(), NotificationIsRead.YES.getValue());
    }

    @Override
    public Long getUserTotalNumberOfUnreadNotifications(String userID, NotificationBroadCast broadCast) {
        Long numberOfUnreadNotifications = notificationDetailRepository
            .countNumberOfNotificationsByReceiverIDOrBroadCastAndIsRead(
                userID, NotificationIsRead.NO.getValue(), broadCast.name());
        return numberOfUnreadNotifications;
    }

    @Override
    public Boolean markAsReadEmployeeNotifications(String userID) {
        log.info("markAsReadEmployeeNotifications(userID={})", userID);
        return this.updateNotificationReadStatusByUserID(
            userID, NotificationBroadCast.ALL_EMPLOYEES, 
            NotificationIsRead.YES.getValue());
    }

    @Override
    public Boolean markAsReadCustomerNotifications(String userID) {
        log.info("markAsReadCustomerNotifications(userID={})", userID);
        return this.updateNotificationReadStatusByUserID(
            userID, NotificationBroadCast.ALL_CUSTOMERS, 
            NotificationIsRead.YES.getValue());
    }

    @Override
    public Boolean updateNotificationReadStatusByUserID(String userID, NotificationBroadCast broadCast, Boolean status){
        log.info("updateNotificationReadStatusByUserID(userID={}, status={})", userID, status);
        return this.notificationUserRepository.updateNotificationReadStatusByUserID(userID, broadCast.name(), status);
    }
}
