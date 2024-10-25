package com.phucx.shop.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails implements Serializable{
    private ProductDetail product;
    private ProductSize size;
}
