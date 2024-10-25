package com.phucx.payment.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PaymentDetails")
public class PaymentDetails {
    @Id
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
