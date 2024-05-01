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
@NamedStoredProcedureQuery(name = "CustomerAccount.createCustomerInfo",
    procedureName = "createCustomerInfo", parameters = {
        @StoredProcedureParameter(name="customerID", mode = ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(name="contactName", mode = ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(name="username", mode = ParameterMode.IN, type=String.class)
    })
@Table(name = "CustomerAccounts")
public class CustomerAccount {
    @Id
    private String userID;
    private String username;
    private String email;
    private String customerID;
    private String contactName;
    // private String companyName;
}