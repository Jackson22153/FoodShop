package com.phucx.order.model;

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
public class DiscountRequest extends DataRequest{
    private String discountID;
    private List<String> discountIDs;
    private List<ProductDiscountsDTO> productsDiscounts;
}
