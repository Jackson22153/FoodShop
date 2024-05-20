package com.phucx.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class User {
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
