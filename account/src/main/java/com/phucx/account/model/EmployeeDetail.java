package com.phucx.account.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
@Immutable
@Table(name = "EmployeeDetails")
@NamedStoredProcedureQuery(name = "EmployeeDetail.updateEmployeeInfo",
    procedureName = "UpdateEmployeeInfo", 
    parameters = {
        @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="email", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="firstName", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="lastName", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="birthDate", mode = ParameterMode.IN, type = LocalDate.class),
        @StoredProcedureParameter(name="address", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="city", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="homePhone", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="photo", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
    })
public class EmployeeDetail {
    @Id
    private String employeeID;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private String homePhone;
    private String photo;
    private String title;
    private String address;
    private String city;


}
