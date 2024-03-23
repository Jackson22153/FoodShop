package com.phucx.shop.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity @Data
@Immutable
@Table(name = "Alphabetical list of products")
public class AlphabeticalListOfProducts {
    @Id
    private Integer productID;
    private String productName;
    private Integer supplierID;
    private Integer categoryID;
    private String quantityPerUnit;
    private Double unitPrice;
    private Integer unitsInStock;
    private Integer unitsOnOrder;
    private Integer reorderLevel;
    private Float discountAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean discontinued;
    private String picture;
    private String categoryName;
    private String companyName;

}
