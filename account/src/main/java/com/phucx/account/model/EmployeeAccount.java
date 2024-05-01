package com.phucx.account.model;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
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
@NamedStoredProcedureQuery(name = "EmployeeAccount.createEmployeeInfo",
    procedureName = "createEmployeeInfo", parameters = {
        @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(name="lastName", mode = ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(name="firstName", mode = ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(name="username", mode = ParameterMode.IN, type=String.class)
    })
@Table(name = "EmployeeAccounts")
public class EmployeeAccount {
    @Id
    private String userID;
    private String username;
    private String email;
    private String employeeID;
    private String firstName;
    private String lastName;
}
