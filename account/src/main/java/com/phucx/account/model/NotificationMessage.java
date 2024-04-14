package com.phucx.account.model;

import com.phucx.account.constant.Notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private String content;
    private Notification status;
}
