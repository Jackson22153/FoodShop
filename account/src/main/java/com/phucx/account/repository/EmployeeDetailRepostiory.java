package com.phucx.account.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Boolean updateEmployeeInfo(String employeeID, String firstName, String lastName, 
        LocalDate birthDate, String address, String city, 
        String phone, String picture);

    @Modifying
    @Transactional
    @Procedure(name = "UpdateAdminEmployeeInfo")
    Boolean updateAdminEmployeeInfo(String employeeID, String firstName, String lastName, 
        LocalDate hireDate, String picture, String title, String notes);

    @Modifying
    @Transactional
    @Procedure(name = "AddNewEmployee")
    public Boolean addNewEmployee(String profileID, String userID, 
        String username, String email, String employeeID, 
        String firstName, String lastName);

    Page<EmployeeDetail> findByEmployeeIDLike(String employeeID, Pageable pageable);
    Page<EmployeeDetail> findByFirstNameLike(String firstName, Pageable pageable);
    Page<EmployeeDetail> findByLastNameLike(String lastName, Pageable pageable);
    Page<EmployeeDetail> findByUsernameLike(String username, Pageable pageable);
    Page<EmployeeDetail> findByEmailLike(String email, Pageable pageable);

    Optional<EmployeeDetail> findByUserID(String userID);
}
