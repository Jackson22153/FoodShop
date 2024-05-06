package com.phucx.shop.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Data @ToString @Immutable
@Table(name = "Sales by Category")
public class SalesByCategory{
    @Id
    private Integer productID;
    private Integer categoryID;
    private String categoryName;
    private String productName;
    private BigDecimal productSales;
}
