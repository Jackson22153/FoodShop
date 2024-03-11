package com.phucx.shop.model;

import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

// @Entity 
@Data @ToString
@Table(name = "Products by Category")
@IdClass(ProductsByCategory.class)
public class ProductsByCategory {
    
}
