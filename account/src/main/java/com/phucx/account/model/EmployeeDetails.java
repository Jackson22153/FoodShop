package com.phucx.account.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetails {
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

    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
