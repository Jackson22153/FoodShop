package com.phucx.account.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailDTO {
    @Id
    private String employeeID;
    private UserInfo userInfo;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    private String homePhone;
    private String photo;
    private String title;
    private String address;
    private String city;
    private String notes;
}
