package com.phucx.account.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Immutable;

import com.phucx.account.constant.DiscountType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Immutable
@Data @ToString
@Table(name = "Current Valid Discounts")
public class CurrentValidDiscount {
    @Id
    private Integer discountID;
    private BigDecimal discountPercent;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean active;
}
