package com.phucx.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.order.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>{
    
}
