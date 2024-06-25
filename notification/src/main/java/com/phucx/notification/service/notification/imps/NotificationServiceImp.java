package com.phucx.notification.service.notification.imps;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.notification.constant.NotificationBroadCast;
import com.phucx.notification.constant.NotificationIsRead;
import com.phucx.notification.exception.NotFoundException;
import com.phucx.notification.model.NotificationDetail;
import com.phucx.notification.repository.NotificationDetailRepository;
import com.phucx.notification.repository.NotificationRepository;
import com.phucx.notification.repository.NotificationUserRepository;
import com.phucx.notification.service.notification.NotificationService;

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
    
        Boolean status = notificationRepository.createNotification(
            notificationID, notification.getTitle(), notification.getMessage(), 
            notification.getSenderID(), notification.getReceiverID(), 
            notification.getTopic(), notification.getStatus(), false, 
            notification.getTime());

        if(!status) throw new RuntimeException("Error during saving the notification: " + notification.toString());
        notification.setNotificationID(notificationID);

        return notification;
    }

    @Override
    public NotificationDetail getNotificationsByID(String notificationID) throws NotFoundException {
        return notificationDetailRepository.findById(notificationID)
            .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " does not found"));
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
    public Boolean updateNotificationReadStatusOfUser(String notificationID, String userID, Boolean isRead) 
    throws NotFoundException {
        log.info("updateNotificationReadStatus(notificationID={}, userID={}, isRead={})", notificationID, userID, isRead);
        return updateNotificationReadStatus(notificationID, userID, isRead);
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
    public NotificationDetail getNotificationByUserIDAndNotificationID(String userID, String notificationID) throws NotFoundException {
        NotificationDetail notification = notificationDetailRepository.findByNotificationIDAndReceiverIDOrderByTimeDesc(notificationID, userID)
            .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " of User " + userID + " does not found"));
        return notification;
    }

    @Override
    public NotificationDetail getNotificationByUserIDOrBroadCastAndNotificationID(String userID, NotificationBroadCast broadCast, String notificationID) 
    throws NotFoundException {
        NotificationDetail notification = notificationDetailRepository
        .findByNotificationIDAndReceiverIDOrBroadCastOrderByTimeDesc(notificationID, userID, broadCast.name())
        .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " of User " + userID + " does not found"));
        return notification;
    }

    @Override
    public Long getUserTotalNumberOfUnreadNotifications(String userID, NotificationBroadCast broadCast) {
        Long numberOfUnreadNotifications = notificationDetailRepository
            .countNumberOfNotificationsByReceiverIDOrBroadCastAndIsRead(
                userID, NotificationIsRead.NO.getValue(), broadCast.name());
        return numberOfUnreadNotifications;
    }

    @Override
    public Boolean updateNotificationsReadStatus(String userID, Boolean status){
        log.info("updateNotificationReadStatus(userID={}, status={})", userID, status);
        return this.notificationUserRepository.updateNotificationReadStatusByUserID(userID, status);
    }

    @Override
    public Boolean updateNotificationReadStatusOfBroadcast(String notificationID, NotificationBroadCast broadCast, Boolean status)
            throws NotFoundException {
        log.info("updateNotificationReadStatusOfBroadcast(notificationID={}, broadCast={}, status={})", notificationID, broadCast, status);
        return updateNotificationReadStatus(notificationID, broadCast.name(), status);
    }

    private Boolean updateNotificationReadStatus(String notificationID, String receiverID, Boolean isRead) throws NotFoundException{
        log.info("updateNotificationReadStatus(notificationID={}, receiverID={}, isRead={})", notificationID, receiverID, isRead);
        // fetch notifiation from db
        NotificationDetail fetchedNotificationDetail = this.notificationDetailRepository
            .findByNotificationIDAndReceiverID(notificationID, receiverID)
            .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " of " + receiverID + " does not found"));
        // update read statu for receiverID
        return notificationUserRepository.updateNotificationReadStatusByNotificationIDAndUserID(
            fetchedNotificationDetail.getNotificationID(), receiverID, isRead);
    }

    @Override
    public NotificationDetail getNotificationByNotificationIDAndReceiverID(String notificationID, String receiverID)
            throws NotFoundException {
        return this.notificationDetailRepository
        .findByNotificationIDAndReceiverID(notificationID, receiverID)
        .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " of " + receiverID + " does not found"));
    }

    @Override
    public Boolean updateNotificationReadStatus(String notificationID, Boolean status) {
        log.info("updateNotificationReadStatus(notificationID={}, status={})", notificationID, status);
        return this.notificationUserRepository.updateNotificationReadStatusByNotificationID(notificationID, status);
    }

    @Override
    public Boolean updateNotificationReadStatusOfBroadcast(String title, String message,
            NotificationBroadCast broadCast, Boolean status) throws NotFoundException {
        log.info("updateNotificationReadStatusOfBroadcast(title={}, message={}, broadCast={}, status={})", title, message, broadCast, status);
        
        Optional<NotificationDetail> optionalNotificationDetail = notificationDetailRepository
            .findByTitleAndReceiverIDAndMessageLike(title, broadCast.name(), "%" + message + "%");

        log.info("Notification: {}", optionalNotificationDetail.get());
        
        NotificationDetail fetchedNotification = notificationDetailRepository
            .findByTitleAndReceiverIDAndMessageLike(title, broadCast.name(), "%" + message + "%")
            .orElseThrow(()-> new NotFoundException("Notification with title " + title + " and message "+ message + " of " + broadCast.name() + " does not found"));
                
        return this.notificationUserRepository.updateNotificationReadStatusByNotificationID(
            fetchedNotification.getNotificationID(), status);
    }
}
