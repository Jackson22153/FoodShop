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
@Table(name = "StoreLocation")
public class StoreLocation {
    @Id
    private String id;
    private String phone;
    private String address;
    private String ward;
    private String district;
    private String city;
}
