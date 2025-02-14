package com.phucx.payment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PaymentMethods")
public class PaymentMethod {
    @Id
    private String methodID;
    private String methodName;
    private String details;
}
