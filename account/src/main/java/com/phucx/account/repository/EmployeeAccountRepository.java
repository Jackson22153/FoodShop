package com.phucx.account.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.EmployeeAccount;





@Repository
public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccount, String>{
    public EmployeeAccount findByUsername(String username);

    @Modifying
    @Transactional
    @Procedure(name = "createEmployeeInfo")
    public void createEmployeeInfo(@Param("employeeID") String employeeID, 
        @Param("lastName") String lastName, @Param("firstName") String firstName,
        @Param("username") String username);

    @Modifying
    @Transactional
    @Procedure(name = "AddNewEmployee")
    public Boolean addNewEmployee(
        @Param("userID") String userID, 
        @Param("username") String username, 
        @Param("password") String password, 
        @Param("email") String email,
        @Param("emailVerified") Boolean emailVerified,
        @Param("enabled") Boolean enabled,
        @Param("employeeID") String employeeID, 
        @Param("firstName") String firstName,
        @Param("lastName") String lastName);

    Optional<EmployeeAccount> findByEmployeeID(String employeeID);

    Page<EmployeeAccount> findByEmailLike(String email, Pageable pageable);
    Page<EmployeeAccount> findByEmployeeIDLike(String employeeID, Pageable pageable);
    Page<EmployeeAccount> findByFirstNameLike(String firstName, Pageable pageable);
    Page<EmployeeAccount> findByLastNameLike(String lastName, Pageable pageable);
    Page<EmployeeAccount> findByUsernameLike(String username, Pageable pageable);
    Optional<EmployeeAccount> findByEmail(String email);

    Optional<EmployeeAccount> findByUserID(String userID);
}
