package com.phucx.account.model;

import com.phucx.account.constant.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String orderID;
    private String customerID;
    private String employeeID;
    
    private OrderStatus orderStatus;
    private Integer pageNumber;
    private Integer pageSize;
    public OrderRequest(String orderID) {
        this.orderID = orderID;
    }
    public OrderRequest(OrderStatus orderStatus, Integer pageNumber, Integer pageSize) {
        this.orderStatus = orderStatus;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
