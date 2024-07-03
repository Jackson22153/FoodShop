package com.phucx.account.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

@Entity
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "User.updateUserPassword", procedureName = "updateUserPassword",
        parameters = {
            @StoredProcedureParameter(name="userID", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name="password", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name="result", type = String.class, mode = ParameterMode.OUT),
        }),
})
public class User implements Serializable{
    @Id
    private String userID;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    public User(String userID, String username, String email) {
        this.userID = userID;
        this.username = username;
        this.email = email;
    }
}
