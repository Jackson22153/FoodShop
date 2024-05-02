package com.phucx.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import com.phucx.account.model.User;

import jakarta.transaction.Transactional;


@Repository
public interface UserRepository extends JpaRepository<User, String>{
    public Optional<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Procedure(name = "UpdateUserPassword")
    Boolean updateUserPassword(String userID, String password);
    
}
