package com.phucx.account.model;

import java.io.Serializable;

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

@Data
@Entity
@Immutable
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CustomerDetails")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "CustomerDetail.updateCustomerInfo",
        procedureName = "UpdateCustomerInfo", parameters = {
            @StoredProcedureParameter(name="customerID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="contactName", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="address", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="city", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="phone", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="picture", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        }),
    @NamedStoredProcedureQuery(name = "CustomerDetail.addNewCustomer",
        procedureName = "AddNewCustomer", parameters = {
            @StoredProcedureParameter(name="profileID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="userID", mode = ParameterMode.IN, type=String.class),
            @StoredProcedureParameter(name="customerID", mode = ParameterMode.IN, type=String.class),
            @StoredProcedureParameter(name="contactName", mode = ParameterMode.IN, type=String.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type=Boolean.class)
        })

})
public class CustomerDetail implements Serializable{
    @Id
    private String customerID;
    private String userID;
    private String contactName;
    private String address;
    private String city;
    private String phone;
    private String picture;
    
    public CustomerDetail(String userID, String contactName) {
        this.userID = userID;
        this.contactName = contactName;
    }
}
