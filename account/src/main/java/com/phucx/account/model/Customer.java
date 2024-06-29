package com.phucx.account.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @Entity @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Customers")
public class Customer implements Serializable{
    @Id
    @Column(name = "CustomerID", length = 36, nullable = false)
    private String customerID;
    @Column(name = "ContactName", length = 30)
    private String contactName;
    @Column(name = "Address", length = 60)
    private String address;
    @Column(name = "City", length = 15)
    private String city;
    @Column(name = "Phone", length = 24)
    private String phone;
    @Column(name = "picture")
    private String picture;

    private String userID;
}
