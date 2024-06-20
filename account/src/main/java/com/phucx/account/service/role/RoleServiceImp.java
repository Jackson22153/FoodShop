package com.phucx.account.service.role;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.account.constant.RoleConstant;
import com.phucx.account.exception.RoleNotFoundException;
import com.phucx.account.model.Role;
import com.phucx.account.repository.RoleRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class RoleServiceImp implements RoleService{
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Role getRole(String roleName) throws RoleNotFoundException {
        Role role = roleRepository.findByRoleName(roleName)
            .orElseThrow(()-> new RoleNotFoundException("Role " + roleName + " does not found"));
        return role;
    }
    @Override
    public Page<Role> getRoles(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return roleRepository.findAll(pageable);
    }
    @Override
    public Page<Role> getRolesWithoutCustomer(Integer pageNumber, Integer pageSize) {
        Page<Role> roles = this.getRoles(pageNumber, pageSize);
        List<Role> filteredRoles = roles.getContent().stream()
            .filter(role -> !role.getRoleName().equalsIgnoreCase(RoleConstant.CUSTOMER.name()))
            .collect(Collectors.toList());

        return new PageImpl<>(filteredRoles, roles.getPageable(), roles.getTotalElements());
    }
    @Override
    public List<Role> getRoles(List<Integer> roleIDs) {
        List<Role> roles = roleRepository.findAllById(roleIDs);
        if(roles.size()!=roleIDs.size()) throw new NotFoundException("Some roles do not found");
        return roles;
    }
    
}
