package com.phucx.account.model;

import java.io.Serializable;

import org.springframework.data.annotation.Immutable;

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
@NamedStoredProcedureQuery(name = "UserRole.assignUserRoles",
    procedureName = "AssignUserRoles", parameters = {
        @StoredProcedureParameter(name="username", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="listRoleID", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
    })
@Table(name = "UserRoles")
public class UserRole implements Serializable{
    @Id
    private String userID;
    @Id
    private Integer roleID;

    private String roleName;
    private String username;

    @Email
    private String email;
}
