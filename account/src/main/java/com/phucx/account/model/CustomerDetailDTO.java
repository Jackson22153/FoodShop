package com.phucx.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailDTO {
    private String customerID;
    private String contactName;
    private String picture;
    private UserInfo userInfo;
}
