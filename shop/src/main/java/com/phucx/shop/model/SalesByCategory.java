package com.phucx.shop.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Data @ToString
@Table(name = "Sales by Category")
@IdClass(SalesByCategory.class)
public class SalesByCategory implements Serializable{
    @Id
    private Integer categoryID;
    @Id
    private String categoryName;
    @Id
    private Integer productID;
    @Id
    private String productName;
    @Id
    private Double productSales;

}
