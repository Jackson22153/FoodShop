package com.phucx.account.service.userRoles;

import java.util.List;

import com.phucx.account.model.UserRoles;

public interface UserRolesService {
    public List<UserRoles> getUserRoles(String userID);
    public boolean assignUserRole(String username, String roleName);
}
