package com.phucx.shop.model;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFilter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

@Data @Entity @ToString
@JsonFilter("OrdersFilter")
public class Orders {
    @Id
    @Column(name = "OrderID", nullable = false)
    private Integer orderID;

    @ManyToOne
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID")
    private Customers customerID;

    @ManyToOne
    @JoinColumn(name = "EmployeeID", referencedColumnName = "EmployeeID")
    private Employees employeeID;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "OrderDate")
    private LocalDateTime orderDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "RequiredDate")
    private LocalDateTime requiredDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "ShippedDate")
    private LocalDateTime shippedDate;

    @ManyToOne
    @JoinColumn(name = "ShipVia", referencedColumnName = "ShipperID")
    private Shippers shipVia;

    private Double freight;

    @Column(name = "ShipName", length = 40)
    private String shipName;

    @Column(name = "ShipAddress", length = 60)
    private String shipAddress;

    @Column(name = "ShipCity", length = 15)
    private String shipCity;

    @Column(name = "ShipRegion", length = 15)
    private String shipRegion;

    @Column(name = "ShipPostalCode", length = 10)
    private String shipPostalCode;

    @Column(name = "ShipCountry", length = 15)
    private String shipCountry;

    private Boolean status;

}
