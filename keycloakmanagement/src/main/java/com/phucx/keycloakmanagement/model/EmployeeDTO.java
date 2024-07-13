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
public class EmployeeDTO extends UserDTO{
    private String employeeID;
    private List<String> employeeIDs;
}
