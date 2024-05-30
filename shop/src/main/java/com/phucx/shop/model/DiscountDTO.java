package com.phucx.shop.model;

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
public class DiscountDTO extends DataDTO{
    private String discountID;
    private List<String> discountIDs;
    private List<ProductDiscountsDTO> productsDiscounts;
}
