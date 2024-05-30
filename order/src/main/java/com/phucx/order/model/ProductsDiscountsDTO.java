package com.phucx.order.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductsDiscountsDTO {
    private List<ProductDiscountsDTO> productsDiscounts;
}
