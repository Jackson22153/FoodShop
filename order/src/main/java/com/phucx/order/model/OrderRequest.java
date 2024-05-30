package com.phucx.order.model;

import com.phucx.order.constant.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest extends DataRequest{
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
