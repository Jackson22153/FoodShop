package com.phucx.account.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDiscount {
    private String discountID;
    private LocalDateTime appliedDate;
    // private String discountType;
}
