package com.phucx.order.model;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Immutable 
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EmployeeAccounts")
public class EmployeeAccount {
    @Id
    private String userID;
    private String username;
    private String email;
    private String employeeID;
    private String firstName;
    private String lastName;
    private String photo;
}
