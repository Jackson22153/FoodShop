package com.phucx.account.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Immutable;

import com.phucx.account.compositeKey.OrderDetailsExtendedID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Immutable
@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Invoices")
@IdClass(OrderDetailsExtendedID.class)
public class Invoice {
    @Id
    private Integer orderID;
    @Id
    private Integer productID;

    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String phone;
    
    private String customerID;

    private String employeeID;
    private String salesperson;

    private LocalDateTime orderDate;
    private LocalDateTime requiredDate;
    private LocalDateTime shippedDate;
    private String shipperName;

    private String productName;
    private String picture;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal extendedPrice;
    private BigDecimal freight;
    
    private String discountID;
    private Integer discountPercent;
    private String discountType;

    private String status;
}
