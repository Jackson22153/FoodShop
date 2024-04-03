package com.phucx.account.service.roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Roles;
import com.phucx.account.repository.RolesRepository;

@Service
public class RolesServiceImp implements RolesService{
    @Autowired
    private RolesRepository rolesRepository;
    @Override
    public Roles getRole(String roleName) {
        Roles role = rolesRepository.findByRoleName(roleName);
        return role;
    }
    
}
