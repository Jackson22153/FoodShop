package com.phucx.account.service.userRole;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Roles;
import com.phucx.account.model.UserRole;
import com.phucx.account.model.User;
import com.phucx.account.repository.RolesRepository;
import com.phucx.account.repository.UserRoleRepository;
import com.phucx.account.repository.UserRepository;

@Service
public class UserRoleServiceImp implements UserRoleService{
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private UserRepository userRepository;
	@Override
	public List<UserRole> getUserRole(String userID) {
		List<UserRole> userRole = userRoleRepository.findByUserID(userID);
        if(userRole!=null) return userRole;
        return new ArrayList<>();
	}
	@Override
	public boolean assignUserRole(String username, String roleName) {
        Roles role = rolesRepository.findByRoleName(roleName);
        User user = userRepository.findByUsername(username);
        if(role!=null && user!=null){
            userRoleRepository.assignUserRole(username, roleName);
            return true;
        }
        return false;
	}
    
}
