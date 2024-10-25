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
public class OrderWithProducts {
    private String orderID;

    private String customerID;
    private String contactName;

    private String employeeID;
    private String salesPerson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requiredDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shippedDate;
    private List<OrderItem> products;

    private Integer shipVia;
    private String shipperName;
    private String shipperPhone;

    private BigDecimal freight;
    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String shipDistrict;
    private String shipWard;
    private String phone;
    private BigDecimal totalPrice;
    private OrderStatus status;
    
    private String method;

    public OrderWithProducts() {
        this.products = new ArrayList<>();
        this.totalPrice=BigDecimal.valueOf(0);
        this.freight = BigDecimal.valueOf(0);
    }
}
