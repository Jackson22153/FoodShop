package com.phucx.account.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRolesUtils {
    private Users user;
    private List<Roles> roles;
}
