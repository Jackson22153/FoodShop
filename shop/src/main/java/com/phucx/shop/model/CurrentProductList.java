package com.phucx.shop.model;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity @Data
@Immutable
@Table(name = "Current Product List")
public class CurrentProductList {
    private Integer productID;
    private String productName;
    private String picture;
}
