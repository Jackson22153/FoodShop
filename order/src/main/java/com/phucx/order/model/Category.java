package com.phucx.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Integer categoryID;
    private String categoryName;
    private String description;
    private String picture;
}
