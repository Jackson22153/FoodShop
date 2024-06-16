// package com.phucx.order.model;

// import java.time.LocalDateTime;

// import com.fasterxml.jackson.annotation.JsonFormat;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.ToString;

// @Data @ToString
// @AllArgsConstructor
// public class NotificationDetail {
//     private String notificationID;
//     private String title;
//     private String message;
//     private String senderID;
//     private String receiverID;

//     private String topic;
//     private Boolean isRead;
//     private String status;
//     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//     private LocalDateTime time;

//     public NotificationDetail(String title, String message, String senderID, String receiverID, String topic, String status) {
//         this();
//         this.title = title;
//         this.message = message;
//         this.senderID = senderID;
//         this.receiverID = receiverID;
//         this.topic = topic;
//         this.status = status;
//     }

//     public NotificationDetail(String title, String message, String senderID, String receiverID, String topic, String status, Boolean isRead) {
//         this();
//         this.title = title;
//         this.message = message;
//         this.senderID = senderID;
//         this.receiverID = receiverID;
//         this.topic = topic;
//         this.status = status;
//         this.isRead = isRead;
//     }

//     public NotificationDetail() {
//         this.isRead = false;
//         this.time = LocalDateTime.now();
//     }

// }
