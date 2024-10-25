package com.phucx.model;

import com.phucx.constant.NotificationStatus;
import com.phucx.constant.NotificationTitle;
import com.phucx.constant.NotificationTopic;

public class NotificationDTO extends DataDTO{
    private String message;
    private String senderID;
    private String receiverID;
    private String picture;
    
    private NotificationTitle title;
    private NotificationTopic topic;
    private NotificationStatus status;
    
    public NotificationDTO() {
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public NotificationTitle getTitle() {
        return title;
    }

    public void setTitle(NotificationTitle title) {
        this.title = title;
    }

    public NotificationTopic getTopic() {
        return topic;
    }

    public void setTopic(NotificationTopic topic) {
        this.topic = topic;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }
}
