package com.phucx.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class Customer{
    private String customerID;
    private String contactName;
    private String address;
    private String city;
    private String phone;
    private String picture;
    private String userID;
}
