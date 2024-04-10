package com.phucx.account.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.phucx.account.constant.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @Entity @ToString
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Integer orderID;

    @ManyToOne
    @JoinColumn(name = "CustomerID", nullable = true)
    private Customers customerID;

    @ManyToOne
    @JoinColumn(name = "EmployeeID", nullable = true)
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

    private Integer shipVia;

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

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Orders(Customers customerID, Employees employeeID, LocalDateTime orderDate, LocalDateTime requiredDate,
            LocalDateTime shippedDate, Integer shipVia, Double freight, String shipName, String shipAddress,
            String shipCity, String shipRegion, String shipPostalCode, String shipCountry, OrderStatus status) {
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipVia = shipVia;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.shipRegion = shipRegion;
        this.shipPostalCode = shipPostalCode;
        this.shipCountry = shipCountry;
        this.status = status;
    }

}
