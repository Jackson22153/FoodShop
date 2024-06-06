package com.phucx.notification.constant;

public enum NotificationActive {
    ACTIVE(true),
    INACTIVE(false);
    
    private Boolean value;
    private NotificationActive(Boolean value){
        this.value = value;
    }

    public Boolean getValue(){
        return this.value;
    }

}
