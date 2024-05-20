package com.phucx.order.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data @Entity @ToString
@Table(name = "Suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "SupplierID", nullable = false)
    private Integer supplierID;
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
    @Column(length = 15, name = "Country")
    private String country;
    @Column(length = 24, name = "Phone")
    private String phone;
    @Column(length = 24, name = "Fax")
    private String fax;
    @Column(name = "HomePage")
    private String homePage;
}
