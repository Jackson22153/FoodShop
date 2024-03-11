package com.phucx.shop.model;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFilter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonFilter("ShippersFilter")
public class Shippers {
    @Id
    @GeneratedValue(generator = "native", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ShipperID", columnDefinition = "ShipperID", nullable = false)
    private Integer shipperID;
    @Column(name = "CompanyName", columnDefinition = "CompanyName", nullable = false, length = 40)
    private String companyName;
    @Column(name = "Phone", length = 24)
    private String phone;
}
