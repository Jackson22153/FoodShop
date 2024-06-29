package com.phucx.account.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetails implements Serializable{
    private String customerID;
    private String contactName;
    private String picture;
    private UserInfo userInfo;
}
