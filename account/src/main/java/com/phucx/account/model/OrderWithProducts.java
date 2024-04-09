package com.phucx.account.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.phucx.account.constraint.OrderStatus;

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

    private OrderStatus status;

    public OrderWithProducts(Orders order){
        this(order.getOrderID(), order.getCustomerID()!=null?order.getCustomerID().getCustomerID():null, 
            order.getEmployeeID()!=null?order.getEmployeeID().getEmployeeID():null, order.getOrderDate(), 
            order.getRequiredDate(), order.getShippedDate(), order.getShipVia(), order.getFreight(), 
            order.getShipName(), order.getShipAddress(), order.getShipCity(), order.getShipRegion(), 
            order.getShipPostalCode(), order.getShipCountry(), order.getStatus());
    }

    public OrderWithProducts(Integer orderID, String customerID, String employeeID, LocalDateTime orderDate,
            LocalDateTime requiredDate, LocalDateTime shippedDate, Integer shipVia, Double freight, String shipName,
            String shipAddress, String shipCity, String shipRegion, String shipPostalCode, String shipCountry,
            OrderStatus status) {
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
        this.status = status;
    }

    public OrderWithProducts(Integer orderID, String customerID, String employeeID, LocalDateTime orderDate,
            LocalDateTime requiredDate, LocalDateTime shippedDate, List<OrderItem> products, Integer shipVia,
            Double freight, String shipName, String shipAddress, String shipCity, String shipRegion,
            String shipPostalCode, String shipCountry, OrderStatus status) {
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
        this.status = status;
    }

    public OrderWithProducts() {
        this.products = new ArrayList<>();
    }
}
