package com.phucx.order.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Immutable;

import com.phucx.order.compositeKey.OrderDetailKey;
import com.phucx.order.constant.OrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@ToString
@Immutable
@NoArgsConstructor
@AllArgsConstructor
@IdClass(OrderDetailKey.class)
@Table(name = "Order Details Extended")
public class OrderDetailExtended {
    @Id
    private String orderID;
    @Id
    private Integer productID;
    private BigDecimal unitPrice;
    private Integer quantity;
    private Integer discount;
    private BigDecimal extendedPrice;
    private BigDecimal freight;
    @Enumerated(EnumType.STRING)
    private OrderStatus status; 
    private String customerID;
    private String employeeID;
}
