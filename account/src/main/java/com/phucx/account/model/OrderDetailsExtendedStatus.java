package com.phucx.account.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Immutable;

import com.phucx.account.compositeKey.OrderDetailsExtendedID;
import com.phucx.account.constant.OrderStatus;

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
@IdClass(OrderDetailsExtendedID.class)
@Table(name = "Order Details Extended Status")
public class OrderDetailsExtendedStatus {
    @Id
    private Integer orderID;
    @Id
    private Integer productID;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private Integer discount;
    private String picture;
    private BigDecimal extendedPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status; 
}
