package com.phucx.shop.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity 
@Immutable
public class ProductDetails{
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

    private Integer discountID;
    private Float discountAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Boolean discontinued;
    private String picture;
    private String categoryName;
    private String companyName;
}
