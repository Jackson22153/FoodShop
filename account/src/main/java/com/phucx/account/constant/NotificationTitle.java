package com.phucx.account.constant;

public enum NotificationTitle {
    USER_INFO_UPDATE("User Infomation");

    private String value;
    private NotificationTitle(String value){
        this.value=value;
    }
    public String getValue(){
        return value;
    }
}
