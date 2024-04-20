package com.phucx.account.compositeKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsExtendedID {
    private Integer orderID;
    private Integer productID;
}
