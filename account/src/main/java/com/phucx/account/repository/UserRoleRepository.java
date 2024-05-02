package com.phucx.account.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.compositeKey.UserRoleID;
import com.phucx.account.model.UserRole;
import java.util.List;


@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleID>{
    List<UserRole> findByUserID(String userID);

    @Modifying
    @Transactional
    @Procedure(name = "AssignUserRoles")
    Boolean assignUserRoles(String username, String listRoleID);

    Page<UserRole> findByEmailLike(String email, Pageable pageable);
    Page<UserRole> findByUserIDLike(String userID, Pageable pageable);
    Page<UserRole> findByRoleNameLike(String roleName, Pageable pageable);
    Page<UserRole> findByUsernameLike(String username, Pageable pageable);
}
