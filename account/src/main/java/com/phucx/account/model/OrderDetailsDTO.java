package com.phucx.account.model;

import java.util.ArrayList;
import java.util.List;

import com.phucx.account.constant.OrderStatus;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class OrderDetailsDTO {
    private Integer orderID;
    private List<ProductDTO> products;
    private Double totalPrice;
    private OrderStatus status;

    public OrderDetailsDTO(Integer orderID, Double totalPrice, OrderStatus status) {
        this.products = new ArrayList<>();
        this.orderID = orderID;
        this.totalPrice = totalPrice;
        this.status = status;
    }
    public OrderDetailsDTO(Integer orderID, List<ProductDTO> products, Double totalPrice, OrderStatus status) {
        this.orderID = orderID;
        this.products = products;
        this.totalPrice = totalPrice;
        this.status = status;
    }
    public OrderDetailsDTO() {
        this.products = new ArrayList<>();
        this.totalPrice = Double.valueOf(0);
    }
}
