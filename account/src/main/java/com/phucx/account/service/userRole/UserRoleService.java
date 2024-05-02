package com.phucx.account.service.userRole;

import java.util.List;

import com.phucx.account.model.UserInfo;
import com.phucx.account.model.UserRole;

public interface UserRoleService {
    public List<UserRole> getUserRole(String userID);
    public boolean assignUserRoles(UserInfo user);
}
