package com.phucx.account.compositeKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRolesID {
    private String userID;
    private String roleName;
}
