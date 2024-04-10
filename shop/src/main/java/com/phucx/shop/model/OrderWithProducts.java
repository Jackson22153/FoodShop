package com.phucx.shop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class OrderWithProducts {
    private Integer orderID;
    private String customerID;
    private String employeeID;
    private LocalDateTime orderDate;
    private LocalDateTime requiredDate;
    private LocalDateTime shippedDate;
    private List<OrderItem> products;
    private Integer shipVia;
    private Double freight;
    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String shipRegion;
    private String shipPostalCode;
    private String shipCountry;

    public OrderWithProducts(Integer orderID, String customerID, String employeeID, LocalDateTime orderDate,
            LocalDateTime requiredDate, LocalDateTime shippedDate, Integer shipVia, Double freight, String shipName,
            String shipAddress, String shipCity, String shipRegion, String shipPostalCode, String shipCountry) {
        this();
        this.orderID = orderID;
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
    }

    public OrderWithProducts(Integer orderID, String customerID, String employeeID, LocalDateTime orderDate,
            LocalDateTime requiredDate, LocalDateTime shippedDate, List<OrderItem> products, Integer shipVia,
            Double freight, String shipName, String shipAddress, String shipCity, String shipRegion,
            String shipPostalCode, String shipCountry) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.products = products;
        this.shipVia = shipVia;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.shipRegion = shipRegion;
        this.shipPostalCode = shipPostalCode;
        this.shipCountry = shipCountry;
    }

    public OrderWithProducts() {
        this.products = new ArrayList<>();
    }
}
