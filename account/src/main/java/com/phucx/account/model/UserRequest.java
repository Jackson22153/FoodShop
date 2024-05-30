package com.phucx.account.model;

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
public class UserRequest extends DataRequest{
    private String username;
    private String userID;
    private String employeeID;
    private String customerID;
    private List<String> customerIDs;
    private Integer shipperID;
    private Integer pageNumber;
    private Integer pageSize;
}
