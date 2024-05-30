package com.phucx.shop.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO extends DataDTO{
    private Integer productID;
    private List<Integer> productIds;
    private List<Product> products;

}
