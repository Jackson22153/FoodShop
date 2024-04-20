package com.phucx.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @Entity @ToString
@AllArgsConstructor
@NoArgsConstructor
public class Customers{
    @Id
    @Column(name = "CustomerID", length = 36, nullable = false)
    private String customerID;
    // @Column(name = "CompanyName", length = 40, nullable = false)
    // private String companyName;
    @Column(name = "ContactName", length = 30)
    private String contactName;
    // @Column(name = "ContactTitle", length = 30)
    // private String contactTitle;
    @Column(name = "Address", length = 60)
    private String address;
    @Column(name = "City", length = 15)
    private String city;
    // @Column(name = "Region", length = 15)
    // private String region;
    // @Column(name = "PostalCode", length = 10)
    // private String postalCode;
    // @Column(name = "Country", length = 15)
    // private String country;
    @Column(name = "Phone", length = 24)
    private String phone;
    // @Column(name = "Fax", length = 24)
    // private String fax;
    @Column(name = "picture")
    private String picture;

    @OneToOne
    @JoinColumn(name = "userID")
    private Users user;
}
