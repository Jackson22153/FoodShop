package com.phucx.order.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest extends DataRequest{
    private Integer productID;
    private List<Integer> productIds;
    private List<Product> products;

}
