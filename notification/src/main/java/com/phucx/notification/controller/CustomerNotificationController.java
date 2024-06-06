package com.phucx.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.notification.model.Notification;
import com.phucx.notification.model.NotificationSummary;
import com.phucx.notification.model.ResponseFormat;
import com.phucx.notification.service.notification.NotificationService;
import javax.naming.NameNotFoundException;

@RestController
@RequestMapping("/customer")
public class CustomerNotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/mark-as-read")
    public ResponseEntity<ResponseFormat> markAsReadNotification(
        @RequestBody Notification notification, Authentication authentication
    ) throws NameNotFoundException{
        Boolean status = notificationService.markAsReadCustomerNotification(
            notification.getNotificationID(), authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @GetMapping("/summary")
    public ResponseEntity<NotificationSummary> getNotificationSummary(Authentication authentication){
        NotificationSummary notificationSummary = new NotificationSummary();
        Long totalOfUnreadNotifications = notificationService.getCustomerNumberOfNotifications(authentication.getName());
        notificationSummary.setTotalOfUnreadNotifications(totalOfUnreadNotifications);
        return ResponseEntity.ok().body(notificationSummary);
    }
    
}
