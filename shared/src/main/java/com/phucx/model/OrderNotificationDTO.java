package com.phucx.model;

import com.phucx.constant.NotificationStatus;
import com.phucx.constant.NotificationTitle;
import com.phucx.constant.NotificationTopic;

public class OrderNotificationDTO extends NotificationDTO{
    private String orderID;
    private Boolean firstNotification;

    public OrderNotificationDTO(String orderID, Boolean firstNotification) {
        this.orderID = orderID;
        this.firstNotification = firstNotification;
    }

    public OrderNotificationDTO(String receiverID, NotificationTitle title, NotificationTopic topic,
            NotificationStatus status, String orderID, Boolean firstNotification) {
        super(receiverID, title, topic, status);
        this.orderID = orderID;
        this.firstNotification = firstNotification;
    }

    public OrderNotificationDTO(String senderID, String receiverID, NotificationTitle title, NotificationTopic topic,
            NotificationStatus status, String orderID, Boolean firstNotification) {
        super(senderID, receiverID, title, topic, status);
        this.orderID = orderID;
        this.firstNotification = firstNotification;
    }

    public OrderNotificationDTO(){
        
    }
    
    public OrderNotificationDTO(String receiverID, NotificationTitle title, NotificationTopic topic,
            NotificationStatus status, String orderID) {
        super(receiverID, title, topic, status);
        this.orderID = orderID;
    }

    public OrderNotificationDTO(String senderID, String receiverID, NotificationTitle title, NotificationTopic topic,
            NotificationStatus status, String orderID) {
        super(senderID, receiverID, title, topic, status);
        this.orderID = orderID;
    }

    public OrderNotificationDTO(String orderID, NotificationTitle title, String senderID, String receiverID,
            NotificationTopic topic, NotificationStatus status) {
        super(senderID, receiverID, title, topic, status);
        this.orderID = orderID;
    }

    public OrderNotificationDTO(String orderID, NotificationTitle title, String receiverID,
            NotificationTopic topic, NotificationStatus status) {
        super(receiverID, title, topic, status);
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Boolean getFirstNotification() {
        return firstNotification;
    }

    public void setFirstNotification(Boolean firstNotification) {
        this.firstNotification = firstNotification;
    }
}
