package com.phucx.account.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.EmployeeDetail;

@Repository
public interface EmployeeDetailRepostiory extends JpaRepository<EmployeeDetail, String> {
    @Modifying
    @Transactional
    @Procedure(name = "UpdateEmployeeInfo")
    Boolean updateEmployeeInfo(String employeeID, String email, String firstName, 
        String lastName, LocalDate birthDate, String address, String city, 
        String homePhone, String photo);
}
