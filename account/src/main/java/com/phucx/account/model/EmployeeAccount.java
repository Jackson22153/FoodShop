package com.phucx.account.model;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
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
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "EmployeeAccount.createEmployeeInfo",
        procedureName = "createEmployeeInfo", parameters = {
            @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type=String.class),
            @StoredProcedureParameter(name="lastName", mode = ParameterMode.IN, type=String.class),
            @StoredProcedureParameter(name="firstName", mode = ParameterMode.IN, type=String.class),
            @StoredProcedureParameter(name="username", mode = ParameterMode.IN, type=String.class)
        }),
    @NamedStoredProcedureQuery(name = "EmployeeAccount.addNewEmployee",
    procedureName = "AddNewEmployee", parameters = {
        @StoredProcedureParameter(name="userID", mode = ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(name="username", mode = ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(name="password", mode = ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(name="email", mode = ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(name="emailVerified", mode = ParameterMode.IN, type=Boolean.class),
        @StoredProcedureParameter(name="enabled", mode = ParameterMode.IN, type=Boolean.class),
        @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(name="firstName", mode = ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(name="lastName", mode = ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type=Boolean.class)
    })
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
    private String photo;
}
