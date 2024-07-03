package com.phucx.account.model;

import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
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

@Entity @Data @ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Employees")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "EmployeeDetail.updateAdminEmployeeInfo",
        procedureName = "UpdateAdminEmployeeInfo",
        parameters = {
            @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="firstName", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="lastName", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="hireDate", mode = ParameterMode.IN, type = LocalDate.class),
            @StoredProcedureParameter(name="photo", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="notes", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        })
})
public class Employee implements Serializable{
    @Id
    @Column(name = "EmployeeID", length = 36, nullable = false)
    private String employeeID;

    @Column(name = "LastName", length = 20, nullable = false)
    private String lastName;

    @Column(name = "FirstName", length = 10, nullable = false)
    private String firstName;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "BirthDate")
    private LocalDate birthDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "HireDate")
    private LocalDate hireDate;
    @Column(name = "Address", length = 200)
    private String address;
    @Column(name = "City", length = 50)
    private String city;

    @Column(name = "HomePhone", length = 24)
    private String homePhone;
    private String photo;
    private String notes;
    private String userID;
}
