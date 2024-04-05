package com.phucx.account.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class UserOrderProducts implements Serializable{
    private String username;
    private String discount;
    private List<Integer> productIDs;
}
