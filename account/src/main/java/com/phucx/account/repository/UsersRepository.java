package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.Users;


@Repository
public interface UsersRepository extends JpaRepository<Users, String>{
    public Users findByUsername(String username);


    // @Modifying
    // @Transactional
    // @Procedure("createUser")
    // public void createUser(String userID, String username, String password);
    
}
