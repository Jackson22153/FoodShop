package com.phucx.notification.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "NotificationDetails")
public class NotificationDetail {
    @Id
    private String notificationID;
    private String title;
    private String message;
    private String senderID;
    private String receiverID;

    private String topic;

    private String status;
    private Boolean isRead;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    public NotificationDetail(String title, String message, String senderID, String receiverID, String topic,
            String status, Boolean isRead, LocalDateTime time) {
        this.title = title;
        this.message = message;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.topic = topic;
        this.status = status;
        this.isRead = isRead;
        this.time = time;
    }
}
