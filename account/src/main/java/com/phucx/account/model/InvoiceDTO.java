package com.phucx.account.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.phucx.account.constant.OrderStatus;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class InvoiceDTO {
    private Integer orderID;

    private List<ProductWithBriefDiscount> products;

    private String customerID;
    private String salesPerson;

    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String phone;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requiredDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shippedDate;
    private String shipperName;

    private Double totalPrice;
    private Double freight;
    private OrderStatus status;
    


    public InvoiceDTO(Integer orderID, String customerID, String salesPerson, String shipName, 
        String shipAddress, String shipCity, LocalDateTime orderDate, LocalDateTime requiredDate,
        LocalDateTime shippedDate, String shipperName, Double freight, OrderStatus status) {

        this();
        this.orderID = orderID;
        this.customerID = customerID;
        this.salesPerson = salesPerson;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipperName = shipperName;
        this.freight = freight;
        this.status = status;
    }



    public InvoiceDTO(Integer orderID, List<ProductWithBriefDiscount> products, String customerID, 
            String salesPerson, String shipName, String shipAddress, String shipCity,LocalDateTime orderDate, 
            LocalDateTime requiredDate, LocalDateTime shippedDate, String shipperName, 
            Double totalPrice, Double freight, OrderStatus status) {
        this.orderID = orderID;
        this.products = products;
        this.customerID = customerID;
        this.salesPerson = salesPerson;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipperName = shipperName;
        this.totalPrice = totalPrice;
        this.freight = freight;
        this.status = status;
    }



    public InvoiceDTO() {
        this.products = new ArrayList<>();
        this.totalPrice = Double.valueOf(0);
    }
}
