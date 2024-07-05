package com.phucx.account.model;

import java.io.Serializable;

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
@Table(name = "UserProfile")
public class UserProfile implements Serializable{
    @Id
    private String profileID;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String phone;
    private String picture;
    private String userID;
}
