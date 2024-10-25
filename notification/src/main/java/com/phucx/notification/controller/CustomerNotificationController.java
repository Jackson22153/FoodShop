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

import com.phucx.constant.NotificationBroadCast;
import com.phucx.notification.constant.WebConstant;
import com.phucx.notification.exception.NotFoundException;
import com.phucx.notification.model.NotificationDetail;
import com.phucx.notification.model.NotificationSummary;
import com.phucx.notification.model.ResponseFormat;
import com.phucx.notification.service.notification.MarkUserNotificationService;
import com.phucx.notification.service.notification.NotificationService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/customer")
public class CustomerNotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MarkUserNotificationService markUserNotificationService;

    @Operation(summary = "Mark notification as read", tags = {"customer", "tutorials", "post"},
        description = "Update notification status")
    @PostMapping("/notification/mark")
    public ResponseEntity<ResponseFormat> markAsReadNotification(
        @RequestParam(name = "type", required = true) String marktype,
        @RequestBody NotificationDetail notification, 
        Authentication authentication
    ) throws NotFoundException{
        Boolean status = markUserNotificationService.markAsReadForCustomer(
            notification.getNotificationID(), authentication.getName(), marktype);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    // GET NOTIFICATIONS
    @Operation(summary = "Get notifications", tags = {"customer", "tutorials", "get"})
    @GetMapping("/notification")
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

    @Operation(summary = "Get notification summary", tags = {"customer", "tutorials", "get"},
        description = "Get number of unread notifications")
    @GetMapping("/summary")
    public ResponseEntity<NotificationSummary> getNotificationSummary(Authentication authentication){
        NotificationSummary notificationSummary = new NotificationSummary();
        Long totalOfUnreadNotifications = notificationService.getUserTotalNumberOfUnreadNotifications(
            authentication.getName(), NotificationBroadCast.ALL_CUSTOMERS);
        notificationSummary.setTotalOfUnreadNotifications(totalOfUnreadNotifications);
        return ResponseEntity.ok().body(notificationSummary);
    }
}
