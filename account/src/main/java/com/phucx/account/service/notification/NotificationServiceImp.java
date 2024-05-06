package com.phucx.account.service.notification;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Notification;
import com.phucx.account.repository.NotificationRepository;

import jakarta.ws.rs.NotFoundException;
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
    public Notification getNotificationsByID(String notificationID) {
        return notificationRepository.findById(notificationID)
            .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " does not found"));
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
    public Boolean updateNotificationActive(String notificationID, Boolean status) {
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
    public Notification getNotificationByUserIDAndNotificationID(String userID, String notificationID) {
        Notification notification = notificationRepository.findByNotificationIDAndReceiverID(notificationID, userID)
            .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " of User " + userID + " does not found"));
        return notification;
    }

    @Override
    public Notification getNotificationByUserIDOrNullAndNotificationID(String userID, String notificationID) {
        Notification notification = notificationRepository.findByNotificationIDAndReceiverIDOrNull(notificationID, userID)
            .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " of User " + userID + " does not found"));
        return notification;
    }
    
}
