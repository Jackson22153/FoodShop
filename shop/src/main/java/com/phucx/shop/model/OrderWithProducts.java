package com.phucx.shop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class OrderWithProducts {
    private String orderID;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requiredDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shippedDate;
    private List<OrderItem> products;

    private Integer shipVia;

    private BigDecimal freight;
    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String shipWard;
    private String shipDistrict;
    private String phone;
    private BigDecimal totalPrice;



    public OrderWithProducts(String orderID, LocalDateTime orderDate, 
            LocalDateTime requiredDate, LocalDateTime shippedDate,
            Integer shipVia, BigDecimal freight, String shipName,
            String shipAddress, String shipCity, String phone) {
        this();
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipVia = shipVia;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
    }


    public OrderWithProducts(String orderID, LocalDateTime orderDate, 
            LocalDateTime requiredDate, LocalDateTime shippedDate,
            Integer shipVia, String shipName, String shipAddress,
            String shipCity, String phone) {
        this();
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipVia = shipVia;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
    }


    public OrderWithProducts(String orderID, LocalDateTime orderDate, 
            LocalDateTime requiredDate, LocalDateTime shippedDate,
            Integer shipVia, BigDecimal freight, String shipName,
            String shipAddress, String shipCity, String phone, BigDecimal totalPrice) {
        this();
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipVia = shipVia;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
        this.totalPrice = totalPrice;
    }


    public OrderWithProducts(String orderID, LocalDateTime orderDate, 
            LocalDateTime requiredDate, LocalDateTime shippedDate, List<OrderItem> products, 
            Integer shipVia, BigDecimal freight, String shipName, String shipAddress, String shipCity, 
            String phone, BigDecimal totalPrice) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.products = products;
        this.shipVia = shipVia;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
        this.totalPrice = totalPrice;
    }

    public OrderWithProducts() {
        this.products = new ArrayList<>();
        this.totalPrice=BigDecimal.valueOf(0);
        this.freight = BigDecimal.valueOf(0);
    }
}
