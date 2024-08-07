package com.phucx.notification.model;

import com.phucx.notification.constant.NotificationStatus;
import com.phucx.notification.constant.NotificationTitle;
import com.phucx.notification.constant.NotificationTopic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO extends DataDTO{
    private String message;
    private String senderID;
    private String receiverID;
    private String picture;

    private NotificationTitle title;
    private NotificationTopic topic;
    private NotificationStatus status;
    
    public NotificationDTO(String receiverID, NotificationTitle title, NotificationTopic topic,
            NotificationStatus status) {
        this.receiverID = receiverID;
        this.title = title;
        this.topic = topic;
        this.status = status;
    }

    public NotificationDTO(String senderID, String receiverID, NotificationTitle title, NotificationTopic topic,
            NotificationStatus status) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.title = title;
        this.topic = topic;
        this.status = status;
    }
}
