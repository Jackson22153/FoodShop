package com.phucx.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipper {
    private Integer shipperID;
    private String companyName;
    private String phone;
}
