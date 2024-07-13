package com.phucx.keycloakmanagement.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends DataDTO{
    private String username;
    private String userID;
    private List<String> userIDs;
}
