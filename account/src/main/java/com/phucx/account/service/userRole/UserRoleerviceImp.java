package com.phucx.account.service.userRole;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Role;
import com.phucx.account.model.UserRole;
import com.phucx.account.model.User;
import com.phucx.account.repository.RoleRepository;
import com.phucx.account.repository.UserRoleRepository;
import com.phucx.account.repository.UserRepository;

@Service
public class UserRoleerviceImp implements UserRoleService{
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
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
        Role role = roleRepository.findByRoleName(roleName);
        User user = userRepository.findByUsername(username);
        if(role!=null && user!=null){
            userRoleRepository.assignUserRole(username, roleName);
            return true;
        }
        return false;
	}
    
}
