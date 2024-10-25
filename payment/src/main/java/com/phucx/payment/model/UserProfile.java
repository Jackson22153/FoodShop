package com.phucx.payment.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile implements Serializable{
    private String address;
    private String city;
    private String district;
    private String ward;
    private String phone;
    private String picture;
    private String userID;
}
