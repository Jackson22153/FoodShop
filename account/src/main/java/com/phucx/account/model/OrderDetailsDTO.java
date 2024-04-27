package com.phucx.account.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.phucx.account.constant.OrderStatus;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class OrderDetailsDTO {
    private Integer orderID;
    private List<ProductDTO> products;
    private BigDecimal totalPrice;
    private OrderStatus status;

    public OrderDetailsDTO(Integer orderID, BigDecimal totalPrice, OrderStatus status) {
        this.products = new ArrayList<>();
        this.orderID = orderID;
        this.totalPrice = totalPrice;
        this.status = status;
    }
    public OrderDetailsDTO(Integer orderID, List<ProductDTO> products, BigDecimal totalPrice, OrderStatus status) {
        this.orderID = orderID;
        this.products = products;
        this.totalPrice = totalPrice;
        this.status = status;
    }
    public OrderDetailsDTO() {
        this.products = new ArrayList<>();
        this.totalPrice = BigDecimal.valueOf(0);
    }
}
