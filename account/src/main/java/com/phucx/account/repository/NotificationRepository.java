package com.phucx.account.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String>{
    
    @Modifying
    @Transactional
    @Procedure("createNotification")
    Boolean createNotification(String notificationID, String title, String message, String senderID, 
        String receiverID, String topicName, String status, Boolean active, LocalDateTime time);

    @Modifying
    @Transactional
    @Procedure("UpdateNotificationStatus")
    Boolean updateNotificationStatus(String notificationID, Boolean active);
    
    Optional<Notification> findByNotificationIDAndReceiverID(String notificationID, String receiverID);

    @Query("""
        SELECT n FROM Notification n \
        WHERE n.notificationID=?1 AND (receiverID=?2 OR receiverID IS NULL)    
            """)
    Optional<Notification> findByNotificationIDAndReceiverIDOrNull(String notificationID, String receiverID);

    @Query("""
        SELECT n FROM Notification n \
        WHERE n.topic.topicName=?1 AND (receiverID=?2 OR receiverID IS NULL)
            """)
    Page<Notification> findByTopicNameAndReceiverID(String topicName, String receiverID, Pageable pageable);

    @Query("""
        SELECT n FROM Notification n WHERE n.topic.topicID=?1
            """)
    Page<Notification> findByTopicID(Integer topicID, Pageable pageable);

    @Query("""
        SELECT n FROM Notification n WHERE n.topic.topicName=?1
            """)
    Page<Notification> findByTopicName(String topicName, Pageable pageable);

    @Query("""
        SELECT n FROM Notification n WHERE n.topic.topicName=?1
            """)
    List<Notification> findByTopicName(String topicName);

    @Query("""
        SELECT n FROM Notification n \
        WHERE (receiverID=?1 OR receiverID IS NULL) \
        ORDER BY time DESC
            """)
    Page<Notification> findByReceiverIDOrNullOrderByTimeDesc(String receiverID, Pageable pageable);

    Page<Notification> findByReceiverIDOrderByTimeDesc(String receiverID, Pageable pageable);
}
