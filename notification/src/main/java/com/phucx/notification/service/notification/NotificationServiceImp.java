package com.phucx.notification.service.notification;

import java.util.List;
import java.util.UUID;
import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.phucx.notification.constant.NotificationActive;
import com.phucx.notification.model.Notification;
import com.phucx.notification.repository.NotificationRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationServiceImp implements NotificationService{
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(Notification notification) {
        log.info("createNotification({})", notification.toString());
        String notificationID = UUID.randomUUID().toString();
        notificationRepository.createNotification(
            notificationID, notification.getTitle(), notification.getMessage(), notification.getSenderID(), 
            notification.getReceiverID(), notification.getTopic().getTopicName(), 
            notification.getStatus(), notification.getActive(), notification.getTime());
        notification.setNotificationID(notificationID);
        return notification;
    }

    @Override
    public Notification getNotificationsByID(String notificationID) throws NameNotFoundException {
        return notificationRepository.findById(notificationID)
            .orElseThrow(()-> new NameNotFoundException("Notification " + notificationID + " does not found"));
    }

    @Override
    public Page<Notification> getNotificationsByTopicName(String topicName, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return notificationRepository.findByTopicName(topicName, pageable);
    }

    @Override
    public List<Notification> getNotificationsByTopicName(String topicName) {
        return notificationRepository.findByTopicName(topicName);
    }

    @Override
    public Page<Notification> getNotificationsByReceiverID(String userID, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return notificationRepository.findByReceiverIDOrderByTimeDesc(userID, pageable);
    }

    @Override
    public Boolean updateNotificationActive(String notificationID, Boolean status) throws NameNotFoundException {
        log.info("updateNotificationActive(notificationID={}, status={})", notificationID, status);
        Notification notification = this.getNotificationsByID(notificationID);
        return notificationRepository.updateNotificationStatus(notification.getNotificationID(), status);
    }

    @Override
    public Page<Notification> getNotificationsByReceiverIDAndTopicName(
        String userID, String topicName, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return notificationRepository.findByTopicNameAndReceiverID(topicName, userID, pageable);
    }

    @Override
    public Page<Notification> getNotificationsByReceiverIDOrNull(String userID, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return notificationRepository.findByReceiverIDOrNullOrderByTimeDesc(userID, pageable);
    }

    @Override
    public Notification getNotificationByUserIDAndNotificationID(String userID, String notificationID) throws NameNotFoundException {
        Notification notification = notificationRepository.findByNotificationIDAndReceiverIDOrderByTimeDesc(notificationID, userID)
            .orElseThrow(()-> new NameNotFoundException("Notification " + notificationID + " of User " + userID + " does not found"));
        return notification;
    }

    @Override
    public Notification getNotificationByUserIDOrNullAndNotificationID(String userID, String notificationID) throws NameNotFoundException {
        Notification notification = notificationRepository.findByNotificationIDAndReceiverIDOrNullOrderByTimeDesc(notificationID, userID)
            .orElseThrow(()-> new NameNotFoundException("Notification " + notificationID + " of User " + userID + " does not found"));
        return notification;
    }

    @Override
    public Boolean markAsReadEmployeeNotification(String notificationID, String userID) throws NameNotFoundException {
        log.info("markAsReadEmployeeNotification(notificationID={}, userID={})", notificationID, userID);
        Notification notification = this.getNotificationByUserIDOrNullAndNotificationID(userID, notificationID);
        return this.updateNotificationActive(notification.getNotificationID(), NotificationActive.INACTIVE.getValue());
    }

    @Override
    public Boolean markAsReadCustomerNotification(String notificationID, String userID) throws NameNotFoundException {
        log.info("markAsReadCustomerNotification(notificationID={}, userID={})", notificationID, userID);
        Notification notification = this.getNotificationByUserIDAndNotificationID(userID, notificationID);
        return this.updateNotificationActive(notification.getNotificationID(), NotificationActive.INACTIVE.getValue());
    }

    @Override
    public Long getEmployeeNumberOfNotifications(String userID) {
        Long numberOfUnreadNotifications = notificationRepository
            .countNumberOfNotificationsByReceiverIDAndActive(
                userID, NotificationActive.INACTIVE.getValue());
        return numberOfUnreadNotifications;
    }

    @Override
    public Long getCustomerNumberOfNotifications(String userID) {
        Long numberOfUnreadNotifications = notificationRepository
            .countNumberOfNotificationsByReceiverIDAndActive(
                userID, NotificationActive.INACTIVE.getValue());
        return numberOfUnreadNotifications;
    }
}
