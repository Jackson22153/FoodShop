package com.phucx.account.model;

import java.util.List;

import org.hibernate.usertype.DynamicParameterizedType.ParameterType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.StoredProcedureParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
// @NamedStoredProcedureQuery(name = "Users.createUser", 
//     procedureName = "createuser", parameters = {
//         @StoredProcedureParameter(name="userID", type = String.class),
//         @StoredProcedureParameter(name="username", type = String.class),
//         @StoredProcedureParameter(name="password", type = String.class),
//     })
public class Users {
    @Id
    private String userID;
    private String username;
    @JsonIgnore
    private String password;
    // @ManyToMany
    // @JoinTable(name = "UserRole",
    //     joinColumns = {
    //         @JoinColumn(name="userID")
    //     },
    //     inverseJoinColumns = {
    //         @JoinColumn(name="roleID")
    //     }
    // )
    // private List<Roles> roles;
}
