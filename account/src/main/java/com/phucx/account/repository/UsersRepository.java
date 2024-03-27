package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.account.model.Users;


@Repository
public interface UsersRepository extends JpaRepository<Users, String>{
    public Users findByUsername(String username);
    
}
