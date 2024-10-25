package com.phucx.model;

public class UserNotificationDTO extends NotificationDTO{
    private String userID;

    public UserNotificationDTO(){
        
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
