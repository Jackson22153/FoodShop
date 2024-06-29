package com.phucx.account.model;

import java.io.Serializable;

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

@Data
@Entity
@Immutable
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CustomerDetails")
@NamedStoredProcedureQuery(name = "CustomerDetail.updateCustomerInfo",
    procedureName = "UpdateCustomerInfo", parameters = {
        @StoredProcedureParameter(name="customerID", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="email", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="contactName", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="address", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="city", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="phone", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="picture", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
    })
public class CustomerDetail implements Serializable{
    @Id
    private String customerID;
    private String userID;
    private String username;
    private String email;
    private String contactName;
    private String address;
    private String city;
    private String phone;
    private String picture;
}
