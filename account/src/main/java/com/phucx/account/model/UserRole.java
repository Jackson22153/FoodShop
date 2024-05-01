package com.phucx.account.model;

import org.springframework.data.annotation.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phucx.account.compositeKey.UserRoleID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.ToString;

@Entity
@Immutable
@Data @ToString
@IdClass(UserRoleID.class)
@NamedStoredProcedureQuery(name = "UserRole.assignUserRole",
    procedureName = "assignUserRole", parameters = {
        @StoredProcedureParameter(name="username", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="roleName", mode = ParameterMode.IN, type = String.class)
    })
@Table(name = "UserRoles")
public class UserRole {
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
