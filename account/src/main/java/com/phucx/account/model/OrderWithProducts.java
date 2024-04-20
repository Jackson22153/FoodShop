package com.phucx.account.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.phucx.account.constant.OrderStatus;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class OrderWithProducts {
    private Integer orderID;
    private String customerID;
    private String employeeID;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requiredDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shippedDate;
    private List<OrderItem> products;
    private Integer shipVia;
    private Double freight;
    private String shipName;
    private String shipAddress;
    private String shipCity;

    private OrderStatus status;

    public OrderWithProducts(Orders order){
        this(order.getOrderID(), order.getCustomer()!=null?order.getCustomer().getCustomerID():null, 
            order.getEmployee()!=null?order.getEmployee().getEmployeeID():null, order.getOrderDate(), 
            order.getRequiredDate(), order.getShippedDate(), order.getShipVia(), order.getFreight(), 
            order.getShipName(), order.getShipAddress(), order.getShipCity(), order.getStatus());
    }

    public OrderWithProducts(Integer orderID, String customerID, String employeeID, LocalDateTime orderDate,
            LocalDateTime requiredDate, LocalDateTime shippedDate, Integer shipVia, Double freight, String shipName,
            String shipAddress, String shipCity, OrderStatus status) {
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
        this.status = status;
    }

    public OrderWithProducts(Integer orderID, String customerID, String employeeID, LocalDateTime orderDate,
            LocalDateTime requiredDate, LocalDateTime shippedDate, List<OrderItem> products, Integer shipVia,
            Double freight, String shipName, String shipAddress, String shipCity, OrderStatus status) {
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
        this.status = status;
    }

    public OrderWithProducts() {
        this.products = new ArrayList<>();
    }
}
