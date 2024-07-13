package com.phucx.keycloakmanagement.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO extends UserDTO{
    private String customerID;
    private List<String> customerIDs;
}
