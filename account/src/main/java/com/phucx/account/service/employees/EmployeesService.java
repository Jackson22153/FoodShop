package com.phucx.account.service.employees;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.phucx.account.model.Employees;

// @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public interface EmployeesService {
    public Employees getEmployeeDetail(String employeeID);
    public boolean createEmployee(Employees employee);
    public boolean updateEmployeeInfo(Employees employee);
    public Page<Employees> findAllEmployees(int pageNumber, int pageSize);
}
