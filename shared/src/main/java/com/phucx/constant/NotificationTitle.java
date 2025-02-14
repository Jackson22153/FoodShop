package com.phucx.constant;

public enum NotificationTitle {
    PLACE_ORDER("Place Order"),
    CONFIRM_ORDER("Confirm Order"),
    RECEIVE_ORDER("Receive Order"),
    CANCEL_ORDER("Cancel Order"),
    FULFILL_ORDER("Fulfill Order"),
    ERROR_ORDER("Error order"),
    INVALID_ORDER("Invalid Order"),

    USER_INFO_UPDATE("User Infomation");

    
    private String value;
    private NotificationTitle(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
    
}
