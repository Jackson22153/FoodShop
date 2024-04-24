package com.phucx.shop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class NotificationCartCookie extends MessageSender{
    private Integer numberOfCartProducts;

    public NotificationCartCookie(String senderID, String recipientID, Integer numberOfCartProducts) {
        super(senderID, recipientID);
        this.numberOfCartProducts = numberOfCartProducts;
    }

    public String getRecipientID(){
        return this.recipientID;
    }

    public String getSenderID(){
        return this.recipientID;
    }
}
