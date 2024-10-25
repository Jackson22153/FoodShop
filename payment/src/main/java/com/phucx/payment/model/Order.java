package com.phucx.payment.model;

import com.phucx.payment.constant.PaymentMethodConstant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderID;
    private String customerID;
    private Double total;
    private PaymentMethodConstant method;
    private String intent;
    private String description;
}
