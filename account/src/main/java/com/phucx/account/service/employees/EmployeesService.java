package com.phucx.account.service.employees;

import org.springframework.data.domain.Page;

import com.phucx.account.model.Employees;

public interface EmployeesService {
    public Employees getEmployeeDetail(String employeeID);
    public boolean createEmployee(String employeeID);
    public boolean updateEmployeeInfo(Employees employee);
    public Page<Employees> findAllEmployees(int pageNumber, int pageSize);
}
