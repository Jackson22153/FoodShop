package com.phucx.account.service.employees;

import org.springframework.data.domain.Page;
import com.phucx.account.model.Employees;

// @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public interface EmployeesService {
    public Employees getEmployeeDetailByID(String employeeID);
    public Employees getEmployeeDetail(String username);
    public boolean createEmployee(Employees employee);
    public boolean updateEmployeeInfo(Employees employee);
    public Page<Employees> findAllEmployees(int pageNumber, int pageSize);
}
