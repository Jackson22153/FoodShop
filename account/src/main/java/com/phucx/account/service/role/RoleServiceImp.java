package com.phucx.account.service.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Role;
import com.phucx.account.repository.RoleRepository;

@Service
public class RoleServiceImp implements RoleService{
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Role getRole(String roleName) {
        Role role = roleRepository.findByRoleName(roleName);
        return role;
    }
    
}
