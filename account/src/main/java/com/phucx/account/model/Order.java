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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @Entity @ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Integer orderID;

    @ManyToOne
    @JoinColumn(name = "CustomerID", nullable = true)
    private Customers customer;

    @ManyToOne
    @JoinColumn(name = "EmployeeID", nullable = true)
    private Employees employee;

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
    private Shipper shipVia;

    private Double freight;

    @Column(name = "ShipName", length = 40)
    private String shipName;

    @Column(name = "ShipAddress", length = 200)
    private String shipAddress;

    @Column(name = "ShipCity", length = 50)
    private String shipCity;

    private String phone;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(Customers customer, Employees employee, LocalDateTime orderDate, LocalDateTime requiredDate,
            LocalDateTime shippedDate, Shipper shipVia, Double freight, String shipName, String shipAddress,
            String shipCity, String phone, OrderStatus status) {
        this.customer = customer;
        this.employee = employee;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipVia = shipVia;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
        this.status = status;
    }

}
