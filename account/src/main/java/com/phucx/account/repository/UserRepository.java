package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.account.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, String>{
    public User findByUsername(String username);


    // @Modifying
    // @Transactional
    // @Procedure("createUser")
    // public void createUser(String userID, String username, String password);
    
}
