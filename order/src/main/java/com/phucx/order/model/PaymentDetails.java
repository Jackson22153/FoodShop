package com.phucx.order.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {
    private String paymentID;
    private LocalDateTime paymentDate;
    private String transactionID;
    private Double amount;
    private String status;
    private String customerID;
    private String orderID;
    private String methodID;
    private String methodName;

}
