package com.phucx.shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Data @ToString
@IdClass(ProductDiscount.class)
@Table(name = "ProductsDiscounts")
public class ProductDiscount {
    @Id
    private Integer productID;
    @Id
    private String discountID;
}
