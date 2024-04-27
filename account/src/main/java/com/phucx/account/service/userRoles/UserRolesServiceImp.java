package com.phucx.account.service.userRoles;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Roles;
import com.phucx.account.model.UserRoles;
import com.phucx.account.model.User;
import com.phucx.account.repository.RolesRepository;
import com.phucx.account.repository.UserRolesRepository;
import com.phucx.account.repository.UserRepository;

@Service
public class UserRolesServiceImp implements UserRolesService{
    @Autowired
    private UserRolesRepository userRolesRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private UserRepository userRepository;
	@Override
	public List<UserRoles> getUserRoles(String userID) {
		List<UserRoles> userRoles = userRolesRepository.findByUserID(userID);
        if(userRoles!=null) return userRoles;
        return new ArrayList<>();
	}
	@Override
	public boolean assignUserRole(String username, String roleName) {
        Roles role = rolesRepository.findByRoleName(roleName);
        User user = userRepository.findByUsername(username);
        if(role!=null && user!=null){
            userRolesRepository.assignUserRole(username, roleName);
            return true;
        }
        return false;
	}
    
}
