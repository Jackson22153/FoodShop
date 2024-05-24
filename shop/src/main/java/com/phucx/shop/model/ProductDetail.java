package com.phucx.shop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Immutable;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity 
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ProductDetails")
public class ProductDetail{
    @Id
    private Integer productID;
    private String productName;
    private Integer categoryID;
    private String quantityPerUnit;
    private BigDecimal unitPrice;
    private Integer unitsInStock;
    private Boolean discontinued;
    private String picture;
    private String description;
    
    private String categoryName;

    private String discountID;
    private Integer discountPercent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}
