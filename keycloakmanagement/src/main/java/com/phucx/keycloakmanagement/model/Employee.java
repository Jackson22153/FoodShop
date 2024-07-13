package com.phucx.keycloakmanagement.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private String employeeID;
    private String userID;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private String phone;
    private String picture;
    private String title;
    private String address;
    private String city;
    private String notes;
}
