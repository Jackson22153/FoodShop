package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.account.model.Roles;


@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer>{
    public Roles findByRoleName(String roleName);
}
