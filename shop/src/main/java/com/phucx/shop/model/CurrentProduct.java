package com.phucx.shop.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Immutable
@Entity @Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Current Product List")
public class CurrentProduct {
    @Id
    private Integer productID;
    private String productName;
    private BigDecimal unitPrice;
    private Integer unitsInStock;
    private String picture;

    private String discountID;
    private Integer discountPercent;

    private String categoryName;
}
