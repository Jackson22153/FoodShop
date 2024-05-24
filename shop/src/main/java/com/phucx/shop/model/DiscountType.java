package com.phucx.shop.model;

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
@Table(name = "DiscountTypes")
public class DiscountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer discountTypeID;
    private String discountType;
    private String description;
}
