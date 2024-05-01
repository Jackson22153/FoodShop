package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.account.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
    public Role findByRoleName(String roleName);
}
