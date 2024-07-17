package com.phucx.account.model;

import com.phucx.account.constant.NotificationStatus;
import com.phucx.account.constant.NotificationTitle;
import com.phucx.account.constant.NotificationTopic;

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

    private NotificationTitle title;
    private NotificationTopic topic;
    private NotificationStatus status;

    private String picture;
    
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
