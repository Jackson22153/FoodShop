package com.phucx.shop.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private String discountID;
    private BigDecimal discountPercent;
    private String discountType;
    private Date startDate;
    private Date endDate;
}
