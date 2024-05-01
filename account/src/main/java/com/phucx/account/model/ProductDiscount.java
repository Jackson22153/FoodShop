package com.phucx.account.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@IdClass(ProductDiscount.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ProductsDiscounts")
public class ProductDiscount {
    @Id
    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;
    @Id
    @ManyToOne
    @JoinColumn(name = "discountID")
    private Discount discount;
}
