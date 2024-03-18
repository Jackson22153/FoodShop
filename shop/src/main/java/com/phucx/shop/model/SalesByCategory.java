package com.phucx.shop.model;

import java.io.Serializable;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Data @ToString @Immutable
@Table(name = "Sales by Category")
public class SalesByCategory implements Serializable{
    @Id
    private Integer productID;
    private Integer categoryID;
    private String categoryName;
    private String productName;
    private Double productSales;
    private String picture;

}
