package com.phucx.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.notification.constant.WebConstant;
import com.phucx.notification.model.Notification;
import com.phucx.notification.service.notification.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
        // notification
    @GetMapping
    public ResponseEntity<Page<Notification>> getNotificationByReceiverID(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        Authentication authentication
    ){
        pageNumber=pageNumber!=null?pageNumber:0;
        Page<Notification> notifications = notificationService.getNotificationsByReceiverID(
            authentication.getName(), pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(notifications);
    }
}
