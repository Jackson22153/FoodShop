package com.phucx.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.notification.constant.NotificationBroadCast;
import com.phucx.notification.constant.WebConstant;
import com.phucx.notification.model.NotificationDetail;
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
        @RequestBody NotificationDetail notification, Authentication authentication
    ) throws NameNotFoundException{
        Boolean status = notificationService.markAsReadCustomerNotification(
            notification.getNotificationID(), authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @PostMapping("/mark-all-as-read")
    public ResponseEntity<ResponseFormat> markAsReadNotifications(Authentication authentication) throws NameNotFoundException{
        Boolean status = notificationService.markAsReadCustomerNotifications(
            authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }
    // GET NOTIFICATIONS
    @GetMapping("/notifications")
    public ResponseEntity<Page<NotificationDetail>> getNotifications(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        Authentication authentication
    ){
        pageNumber=pageNumber!=null?pageNumber:0;
        Page<NotificationDetail> notifications = notificationService
            .getNotificationsByReceiverIDOrBroadCast(authentication.getName(), 
                NotificationBroadCast.ALL_CUSTOMERS, 
                pageNumber, WebConstant.NOTIFICATION_PAGE_SIZE);
        return ResponseEntity.ok().body(notifications);
    }

    @GetMapping("/summary")
    public ResponseEntity<NotificationSummary> getNotificationSummary(Authentication authentication){
        NotificationSummary notificationSummary = new NotificationSummary();
        Long totalOfUnreadNotifications = notificationService.getUserTotalNumberOfUnreadNotifications(
            authentication.getName(), NotificationBroadCast.ALL_CUSTOMERS);
        notificationSummary.setTotalOfUnreadNotifications(totalOfUnreadNotifications);
        return ResponseEntity.ok().body(notificationSummary);
    }
}
