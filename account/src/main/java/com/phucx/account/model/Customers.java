package com.phucx.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data @Entity @ToString
public class Customers{
    @Id
    @Column(name = "CustomerID", length = 36, nullable = false)
    private String customerID;
    @Column(name = "CompanyName", length = 40, nullable = false)
    private String companyName;
    @Column(name = "ContactName", length = 30)
    private String contactName;
    @Column(name = "ContactTitle", length = 30)
    private String contactTitle;
    @Column(name = "Address", length = 60)
    private String address;
    @Column(name = "City", length = 15)
    private String city;
    @Column(name = "Region", length = 15)
    private String region;
    @Column(name = "PostalCode", length = 10)
    private String postalCode;
    @Column(name = "Country", length = 15)
    private String country;
    @Column(name = "Phone", length = 24)
    private String phone;
    @Column(name = "Fax", length = 24)
    private String fax;
    @Column(name = "picture")
    private String picture;

    public Customers(String customerID, String companyName, String contactName, String contactTitle, String address,
            String city, String region, String postalCode, String country, String phone, String fax, String picture) {
        this.customerID = customerID;
        this.companyName = companyName;
        this.contactName = contactName;
        this.contactTitle = contactTitle;
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
        this.fax = fax;
        this.picture = picture;
    }

    public Customers(String customerID, String companyName, String contactName, String contactTitle, String address,
            String city, String region, String postalCode, String country, String phone, String fax) {
        this.customerID = customerID;
        this.companyName = companyName;
        this.contactName = contactName;
        this.contactTitle = contactTitle;
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
        this.fax = fax;
    }

    public Customers() {
    }
}
