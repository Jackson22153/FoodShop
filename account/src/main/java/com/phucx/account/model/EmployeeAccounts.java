package com.phucx.account.model;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Immutable 
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAccounts {
    @Id
    private String userID;
    private String username;
    private String email;
    private String employeeID;
    private String firstName;
    private String lastName;
}
