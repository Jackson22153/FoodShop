package com.phucx.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Query("""
        SELECT u FROM User u JOIN Customer c ON u.userID=c.userID \
        WHERE c.customerID=?1
            """)
    Optional<User> findByCustomerID(String customerID);

    @Query("""
        SELECT u FROM User u JOIN Employee e ON u.userID=e.userID \
        WHERE e.employeeID=?1
            """)
    Optional<User> findByEmployeeID(String employeeID);
    
}
