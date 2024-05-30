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
public class OrderDTO extends DataDTO{
    private String orderID;
    private String customerID;
    private String employeeID;
    
    private OrderStatus orderStatus;
    private Integer pageNumber;
    private Integer pageSize;
    public OrderDTO(String orderID) {
        this.orderID = orderID;
    }
    public OrderDTO(OrderStatus orderStatus, Integer pageNumber, Integer pageSize) {
        this.orderStatus = orderStatus;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
