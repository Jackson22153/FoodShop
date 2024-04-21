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

    private Double freight;
    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String phone;
    private Double totalPrice;
    private OrderStatus status;



    public OrderWithProducts(Integer orderID, String customerID, String contactName, String employeeID,
            String salesPerson, LocalDateTime orderDate, LocalDateTime requiredDate, LocalDateTime shippedDate,
            Integer shipVia, String shipperName, String shipperPhone, Double freight, String shipName,
            String shipAddress, String shipCity, String phone, OrderStatus status) {
        this();
        this.orderID = orderID;
        this.customerID = customerID;
        this.contactName = contactName;
        this.employeeID = employeeID;
        this.salesPerson = salesPerson;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipVia = shipVia;
        this.shipperName = shipperName;
        this.shipperPhone = shipperPhone;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
        this.status = status;
    }


    public OrderWithProducts(Integer orderID, String customerID, String contactName, String employeeID,
            String salesPerson, LocalDateTime orderDate, LocalDateTime requiredDate, LocalDateTime shippedDate,
            Integer shipVia, String shipperName, String shipperPhone, String shipName, String shipAddress,
            String shipCity, String phone, OrderStatus status) {
        this();
        this.orderID = orderID;
        this.customerID = customerID;
        this.contactName = contactName;
        this.employeeID = employeeID;
        this.salesPerson = salesPerson;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipVia = shipVia;
        this.shipperName = shipperName;
        this.shipperPhone = shipperPhone;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
        this.status = status;
    }


    public OrderWithProducts(Integer orderID, String customerID, String contactName, String employeeID,
            String salesPerson, LocalDateTime orderDate, LocalDateTime requiredDate, LocalDateTime shippedDate,
            Integer shipVia, String shipperName, String shipperPhone, Double freight, String shipName,
            String shipAddress, String shipCity, String phone, Double totalPrice, OrderStatus status) {
        this();
        this.orderID = orderID;
        this.customerID = customerID;
        this.contactName = contactName;
        this.employeeID = employeeID;
        this.salesPerson = salesPerson;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipVia = shipVia;
        this.shipperName = shipperName;
        this.shipperPhone = shipperPhone;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
        this.totalPrice = totalPrice;
        this.status = status;
    }


    public OrderWithProducts(Integer orderID, String customerID, String contactName, String employeeID,
            String salesPerson, LocalDateTime orderDate, LocalDateTime requiredDate, LocalDateTime shippedDate,
            List<OrderItem> products, Integer shipVia, String shipperName, String shipperPhone, Double freight,
            String shipName, String shipAddress, String shipCity, String phone, Double totalPrice, OrderStatus status) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.contactName = contactName;
        this.employeeID = employeeID;
        this.salesPerson = salesPerson;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.products = products;
        this.shipVia = shipVia;
        this.shipperName = shipperName;
        this.shipperPhone = shipperPhone;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
        this.totalPrice = totalPrice;
        this.status = status;
    }


    // public OrderWithProducts(Order order){
    //     this(order.getOrderID(), order.getCustomer()!=null?order.getCustomer().getCustomerID():null, 
    //         order.getCustomer().getContactName(), order.getEmployee()!=null?order.getEmployee().getEmployeeID():null, 
    //         order.getEmployee().getFirstName() + " " + order.getEmployee().getLastName(), order.getOrderDate(), 
    //         order.getRequiredDate(), order.getShippedDate(), order.getShipVia().getShipperID(), 
    //         order.getShipVia().getCompanyName(), order.getShipVia().getPhone(), order.getFreight(), 
    //         order.getShipName(), order.getShipAddress(), order.getShipCity(), order.getPhone(), 
    //         order.getStatus());
    // }


    public OrderWithProducts() {
        this.products = new ArrayList<>();
        this.totalPrice=Double.valueOf(0);
        this.freight = Double.valueOf(0);
    }
}
