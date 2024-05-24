package com.phucx.shop.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data @Entity 
@NoArgsConstructor
@AllArgsConstructor
public class Customer{
    private String customerID;
    private String contactName;
    private String address;
    private String city;
    private String phone;
    private String picture;
    private String userID;
}
