package com.phucx.order.model;

import java.math.BigDecimal;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @Entity @ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(generator = "native", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ProductID", nullable = false)
    private Integer productID;

    @Column(name = "ProductName", length = 40, nullable = false)
    private String productName;

    private String supplierID;

    private String categoryID;

    @Column(name = "QuantityPerUnit", length = 20)
    private String quantityPerUnit;

    @Column(name = "UnitPrice")
    private BigDecimal unitPrice;

    @Column(name = "UnitsInStock")
    private Integer unitsInStock;

    @Column(name = "UnitsOnOrder")
    private Integer unitsOnOrder;

    @Column(name = "ReorderLevel")
    private Integer reorderLevel;

    @Column(name = "Discontinued", nullable = false)
    private Boolean discontinued;

    private String picture;
}