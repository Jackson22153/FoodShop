package com.phucx.order.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.phucx.order.constant.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
public class InvoiceDTO {
    private Integer orderID;

    private List<ProductWithBriefDiscount> products;

    private String customerID;

    private String employeeID;
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

    private BigDecimal totalPrice;
    private BigDecimal freight;
    private OrderStatus status;

    public InvoiceDTO(Integer orderID, String customerID, String employeeID, String salesPerson, String shipName,
            String shipAddress, String shipCity, String phone, LocalDateTime orderDate, LocalDateTime requiredDate,
            LocalDateTime shippedDate, String shipperName, BigDecimal freight, OrderStatus status) {
        this();
        this.orderID = orderID;
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.salesPerson = salesPerson;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipperName = shipperName;
        this.freight = freight;
        this.status = status;
    }

    public InvoiceDTO() {
        this.products = new ArrayList<>();
        this.totalPrice = BigDecimal.valueOf(0);
    }
}
