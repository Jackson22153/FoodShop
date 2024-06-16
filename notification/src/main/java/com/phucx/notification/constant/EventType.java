package com.phucx.notification.constant;

public enum EventType {
    // notification
    // send notification to a user
    SendCustomerNotificationToUser,
    SendOrderNotificationToUser,
    SendEmployeeNotificationToUser,

    

    // udpate order status
    MarkOrderAsConfirmed,
    ReturnStatusMarkOrderAsConfirmed
}
