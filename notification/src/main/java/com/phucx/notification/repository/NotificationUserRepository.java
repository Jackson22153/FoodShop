package com.phucx.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.notification.compositekey.NotificationUserKey;
import com.phucx.notification.model.NotificationUser;

@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, NotificationUserKey>{
    @Modifying
    @Transactional
    @Procedure("UpdateNotificationReadStatusByNotificationIDAndUserID")
    Boolean updateNotificationReadStatusByNotificationIDAndUserID(String notificationID, String userID, Boolean isRead);
    
    @Modifying
    @Transactional
    @Procedure("UpdateNotificationReadStatusByUserID")
    Boolean updateNotificationReadStatusByUserID(String userID, Boolean isRead);

    @Modifying
    @Transactional
    @Procedure("UpdateNotificationReadStatusByNotificationID")
    Boolean updateNotificationReadStatusByNotificationID(String notificationID, Boolean isRead);

    @Modifying
    @Transactional
    @Procedure("UpdateNotificationsReadByNotificationID")
    Boolean updateNotificationsReadByNotificationID(String notificationIDs, Boolean isRead);
}
