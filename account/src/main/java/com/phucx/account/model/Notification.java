package com.phucx.account.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Entity
@Data @ToString
@AllArgsConstructor
@Table(name = "Notifications")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
        name = "Notification.createNotification", 
        procedureName = "createNotification",
        parameters = {
            @StoredProcedureParameter(name="notificationID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="title", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="message", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="senderID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="receiverID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="topicName", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="status", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="active", mode = ParameterMode.IN, type = Boolean.class),
            @StoredProcedureParameter(name="time", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        }),
    @NamedStoredProcedureQuery(
        name = "Notification.UpdateNotificationStatus",
        procedureName = "UpdateNotificationStatus",
        parameters = {
            @StoredProcedureParameter(name="notificationID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="active", mode = ParameterMode.IN, type = Boolean.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class)
        }
    )
})
public class Notification {
    @Id
    private String notificationID;
    private String title;
    private String message;
    private String senderID;
    private String receiverID;
    @OneToOne
    @JoinColumn(name = "topicID")
    private Topic topic;

    private String status;
    private Boolean active;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    public Notification(String title, String message, String senderID, String receiverID, Topic topic, String status) {
        this();
        this.title = title;
        this.message = message;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.topic = topic;
        this.status = status;
    }

    public Notification(String title, String message, String senderID, String receiverID, Topic topic, String status, Boolean active) {
        this();
        this.title = title;
        this.message = message;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.topic = topic;
        this.status = status;
        this.active = active;
    }

    public Notification() {
        this.active = true;
        this.time = LocalDateTime.now();
    }

}
