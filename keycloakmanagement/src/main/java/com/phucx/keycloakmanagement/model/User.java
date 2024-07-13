package com.phucx.keycloakmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userID;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String picture;
    
    public User(String userID, String username, String email, String firstName, String lastName) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
