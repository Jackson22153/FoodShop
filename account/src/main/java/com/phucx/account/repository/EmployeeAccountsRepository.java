package com.phucx.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.EmployeeAccounts;



@Repository
public interface EmployeeAccountsRepository extends JpaRepository<EmployeeAccounts, String>{
    public EmployeeAccounts findByUsername(String username);

    @Modifying
    @Transactional
    @Procedure(name = "createEmployeeInfo")
    public void createEmployeeInfo(@Param("employeeID") String employeeID, 
        @Param("lastName") String lastName, @Param("firstName") String firstName,
        @Param("username") String username);

    Optional<EmployeeAccounts> findByEmployeeID(String employeeID);
}
