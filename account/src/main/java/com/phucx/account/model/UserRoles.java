package com.phucx.account.model;

import org.springframework.data.annotation.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phucx.account.compositeKey.UserRolesID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.ToString;

@Entity
@Immutable
@Data @ToString
@IdClass(UserRolesID.class)
@NamedStoredProcedureQuery(name = "UserRoles.assignUserRole",
    procedureName = "assignUserRole", parameters = {
        @StoredProcedureParameter(name="username", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="roleName", mode = ParameterMode.IN, type = String.class)
    })
public class UserRoles {
    @Id
    private String userID;
    @Id
    private String roleName;
    private String username;
    @JsonIgnore
    private String password;
    @Email
    private String email;
}
