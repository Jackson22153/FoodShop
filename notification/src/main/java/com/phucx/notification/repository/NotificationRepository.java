package com.phucx.notification.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.notification.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String>{
    
    @Modifying
    @Transactional
    @Procedure("createNotification")
    Boolean createNotification(String notificationID, String title, String message, String senderID, 
        String receiverID, String topicName, String status, Boolean isRead, LocalDateTime time);
}
