package com.phucx.account.service.role;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.account.model.Role;

public interface RoleService {
    Role getRole(String roleName);
    Page<Role> getRoles(Integer pageNumber, Integer pageSize);
    Page<Role> getRolesWithoutCustomer(Integer pageNumber, Integer pageSize);
    List<Role> getRoles(List<Integer> roleIDs);
}
