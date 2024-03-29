package com.phucx.account.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Integer roleID;

    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private List<Users> users;
}
