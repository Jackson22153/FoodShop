package com.phucx.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String username;
    private String userID;
    private String employeeID;
    private String customerID;
    private Integer shipperID;
    private Integer pageNumber;
    private Integer pageSize;
}
