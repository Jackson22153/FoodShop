package com.phucx.account.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.Employee;

import jakarta.validation.constraints.Size;
import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>{
    
    @Modifying
    @Transactional
    @Procedure(name = "UpdateAdminEmployeeInfo")
    Boolean updateAdminEmployeeInfo(String employeeID, String firstName, 
        String lastName, LocalDate hireDate, String photo, String notes);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Employee \
            SET firstName=?1, lastName=?2, birthDate=?3, address=?4, \
                city=?5, homePhone=?6, photo=?7 \
            WHERE employeeID=?8\
            """)
    Integer updateEmployee(@Size(max = 10)String firstName, @Size(max = 20) String lastName, 
        LocalDate birthDate, @Size(max = 200)String address,@Size(max = 50) String city,
        @Size(max = 24)String homePhone, String photo, @Size(min = 36, max = 36) String employeeID);

    Optional<Employee> findByUserID(String userID);
}
