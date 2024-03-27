package com.phucx.account.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    private String userID;
    private String username;
    @JsonIgnore
    private String password;
    @ManyToMany
    @JoinTable(name = "UserRole",
        joinColumns = {
            @JoinColumn(name="userID")
        },
        inverseJoinColumns = {
            @JoinColumn(name="roleID")
        }
    )
    private List<Roles> roles;
}
