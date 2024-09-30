package com.phucx.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private String paymentID;
    private Double amount;
    private String orderID;
    private String method;
    private String customerID;
    private String baseUrl;
}
