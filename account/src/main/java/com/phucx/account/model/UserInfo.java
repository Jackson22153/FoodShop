package com.phucx.account.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class UserInfo {
    private User user;
    private List<Role> roles;
 
    public UserInfo(User user) {
        this();
        this.user = user;
    }

    public UserInfo(User user, List<Role> roles) {
        this.user = user;
        this.roles = roles;
    }

    public UserInfo() {
        this.roles = new ArrayList<>();
    }


}
