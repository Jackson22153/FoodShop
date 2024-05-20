package com.phucx.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.order.model.EmployeeAccount;

@Repository
public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccount, String>{
    Optional<EmployeeAccount> findByUsername(String username);

    Optional<EmployeeAccount> findByEmployeeID(String employeeID);

    Optional<EmployeeAccount> findByEmail(String email);

    Optional<EmployeeAccount> findByUserID(String userID);
}
