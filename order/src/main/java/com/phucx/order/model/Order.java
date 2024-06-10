package com.phucx.order.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.phucx.order.constant.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @Entity @ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orders")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "Order.insertOrder", procedureName = "InsertOrder",
        parameters = {
            @StoredProcedureParameter(name="orderID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="orderDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="requiredDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="shippedDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="freight", mode = ParameterMode.IN, type = BigDecimal.class),
            @StoredProcedureParameter(name="shipName", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="shipAddress", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="shipCity", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="phone", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="status", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="customerID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="shipperID", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        })
})
public class Order {
    @Id
    private String orderID;

    private String customerID;

    private String employeeID;

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

    private BigDecimal freight;

    @Column(name = "ShipName", length = 40)
    private String shipName;

    @Column(name = "ShipAddress", length = 200)
    private String shipAddress;

    @Column(name = "ShipCity", length = 50)
    private String shipCity;

    private String phone;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(String customerID, String employeeID, LocalDateTime orderDate, LocalDateTime requiredDate,
            LocalDateTime shippedDate, Integer shipVia, BigDecimal freight, String shipName, String shipAddress,
            String shipCity, String phone, OrderStatus status) {
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
        this.phone = phone;
        this.status = status;
    }

}
