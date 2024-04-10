package com.phucx.account.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@IdClass(ProductsDiscounts.class)
@AllArgsConstructor
@NoArgsConstructor
public class ProductsDiscounts {
    @Id
    @ManyToOne
    @JoinColumn(name = "productID")
    private Products productID;
    @Id
    @ManyToOne
    @JoinColumn(name = "discountID")
    private Discount discountID;
}
