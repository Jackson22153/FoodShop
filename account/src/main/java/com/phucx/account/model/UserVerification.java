package com.phucx.account.model;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UserVerification")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "UserVerification.updatePhoneVerification", procedureName = "UpdatePhoneVerification",
    parameters = {
        @StoredProcedureParameter(name="profileID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="phoneVerification", type = Boolean.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    }),
    @NamedStoredProcedureQuery(name = "UserVerification.updateProfileVerification", procedureName = "UpdateProfileVerification",
    parameters = {
        @StoredProcedureParameter(name="profileID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="profileVerification", type = Boolean.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    })
})
public class UserVerification {
    @Id
    private String id;
    private Boolean phoneVerification;
    private Boolean profileVerification;
    private String profileID;
}
