package com.phucx.account.model;

import java.io.Serializable;
import java.time.LocalDate;

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

@Data
@Entity
@ToString
@Immutable
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EmployeeDetails")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "EmployeeDetail.updateAdminEmployeeInfo",
    procedureName = "UpdateAdminEmployeeInfo", parameters = {
        @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="hireDate", mode = ParameterMode.IN, type = LocalDate.class),
        @StoredProcedureParameter(name="picture", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="title", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="notes", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
    }),
    @NamedStoredProcedureQuery(name = "EmployeeDetail.updateEmployeeInfo",
        procedureName = "UpdateEmployeeInfo", parameters = {
            @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="birthDate", mode = ParameterMode.IN, type = LocalDate.class),
            @StoredProcedureParameter(name="address", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="city", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="phone", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="picture", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        }),
    @NamedStoredProcedureQuery(name = "EmployeeAccount.addNewEmployee",
        procedureName = "AddNewEmployee", parameters = {
            @StoredProcedureParameter(name="profileID", mode = ParameterMode.IN, type=String.class),
            @StoredProcedureParameter(name="userID", mode = ParameterMode.IN, type=String.class),
            @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type=String.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type=Boolean.class)
        })
})
public class EmployeeDetail implements Serializable{
    @Id
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
    public EmployeeDetail(String userID) {
        this.userID = userID;
    }
    public EmployeeDetail(String employeeID, String userID) {
        this.employeeID = employeeID;
        this.userID = userID;
    }
}
