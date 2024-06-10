package com.phucx.notification.constant;

public enum NotificationIsRead {
    YES(true),
    NO(false);
    
    private Boolean value;
    private NotificationIsRead(Boolean value){
        this.value = value;
    }

    public Boolean getValue(){
        return this.value;
    }

}
