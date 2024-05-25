package com.phucx.shop.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Discounts")
public class Discount {
    @Id
    private String discountID;
    private Integer discountPercent;
    private Integer discountTypeID;
    private String discountCode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean active;
}
