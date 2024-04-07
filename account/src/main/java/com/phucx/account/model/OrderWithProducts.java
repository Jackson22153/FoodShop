package com.phucx.account.model;

import java.time.LocalDateTime;
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
    private List<Integer> productIDs;
    private Integer shipVia;
    private Double freight;
    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String shipRegion;
    private String shipPostalCode;
    private String shipCountry;

    private Boolean status;
}
