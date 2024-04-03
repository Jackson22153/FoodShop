package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.compositeKey.UserRolesID;
import com.phucx.account.model.UserRoles;
import java.util.List;


@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, UserRolesID>{
    public List<UserRoles> findByUserID(String userID);

    @Modifying
    @Transactional
    @Procedure(name = "assignUserRole")
    public void assignUserRole(String username, String roleName);
}
