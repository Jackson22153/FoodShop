package com.phucx.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.account.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
    public Optional<Role> findByRoleName(String roleName);
}
