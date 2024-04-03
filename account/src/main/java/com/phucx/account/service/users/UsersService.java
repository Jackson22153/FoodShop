package com.phucx.account.service.users;

import com.phucx.account.model.UserRolesUtils;
import com.phucx.account.model.Users;

public interface UsersService {
    public Users getUser(String username);
    public Users getUserByID(String userID);

    public boolean createUser(Users user);

    public UserRolesUtils getUserRoles(String userID);    
}
